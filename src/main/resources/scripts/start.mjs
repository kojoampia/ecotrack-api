import { spawn } from 'node:child_process';
import { existsSync, readdirSync, readFileSync, statSync } from 'node:fs';
import os from 'node:os';
import { join, resolve } from 'node:path';

function getNgBin() {
  const localBin = resolve(process.cwd(), 'node_modules', '.bin', process.platform === 'win32' ? 'ng.cmd' : 'ng');
  return existsSync(localBin) ? localBin : 'ng';
}

function run(command, args) {
  const child = spawn(command, args, { stdio: 'inherit' });
  child.on('exit', code => process.exit(code ?? 1));
  child.on('error', error => {
    console.error(error);
    process.exit(1);
  });
}

function findLatestAngularErrorLog({ notOlderThanMs }) {
  const tmpDir = os.tmpdir();
  let latestPath = null;
  let latestMtimeMs = -1;

  for (const entry of readdirSync(tmpDir, { withFileTypes: true })) {
    if (!entry.isDirectory()) continue;
    if (!entry.name.startsWith('ng-')) continue;

    const candidate = join(tmpDir, entry.name, 'angular-errors.log');
    if (!existsSync(candidate)) continue;

    const { mtimeMs } = statSync(candidate);
    if (mtimeMs < notOlderThanMs) continue;

    if (mtimeMs > latestMtimeMs) {
      latestMtimeMs = mtimeMs;
      latestPath = candidate;
    }
  }

  return latestPath;
}

function runWithFallback(primary, fallback, { fallbackWhen }) {
  const startedAtMs = Date.now();
  const child = spawn(primary.command, primary.args, { stdio: 'inherit' });

  child.on('exit', code => {
    if (code === 0) process.exit(0);

    const logPath = findLatestAngularErrorLog({ notOlderThanMs: startedAtMs - 5000 });
    const logOutput = logPath ? readFileSync(logPath, 'utf8') : '';

    if (fallbackWhen({ code: code ?? 1, output: logOutput })) {
      console.warn(
        '\n`ng serve` could not start (port binding not permitted in this environment). Falling back to a one-off `ng build` so you still get compile errors.\n',
      );
      run(fallback.command, fallback.args);
      return;
    }
    process.exit(code ?? 1);
  });

  child.on('error', error => {
    if (fallbackWhen({ code: 1, output: String(error) })) {
      console.warn(
        '\n`ng serve` could not start (port binding not permitted in this environment). Falling back to a one-off `ng build` so you still get compile errors.\n',
      );
      run(fallback.command, fallback.args);
      return;
    }
    console.error(error);
    process.exit(1);
  });
}

const ng = getNgBin();
const passthroughArgs = process.argv.slice(2);

runWithFallback(
  { command: ng, args: ['serve', '--hmr', ...passthroughArgs] },
  { command: ng, args: ['build', '--configuration', 'development', ...passthroughArgs] },
  {
    fallbackWhen: ({ output }) => output.includes('listen EPERM: operation not permitted') || output.includes('EACCES'),
  },
);
