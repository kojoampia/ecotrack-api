pipeline {
    agent any

    triggers {
        pollSCM('H H * * *')
    }

    environment {
        REGISTRY = "docker.jojoaddison.net"
        IMAGE_NAME = "ecotrackapi"
        // Jenkins credentials ID for your Docker registry
        REGISTRY_CREDENTIALS_ID = "docker-jojoaddison-net-credentials"
        // Jenkins credentials ID for your Git repository
        GIT_CREDENTIALS_ID = "kojo-git-ssh-priv-key"
        // Version
        BUILD_NUMBER = "2026.01.${BUILD_NUMBER}"
    }

    stages {
        stage('Build Docker Image with Jib') {
            steps {
                script {
                    def imageName = "${REGISTRY}/${IMAGE_NAME}"
                    def imageURL = "${imageName}:${env.BUILD_NUMBER}"
                    echo "Building Docker image: ${imageURL}"
                    sh "./mvnw clean package jib:dockerBuild -DskipTests"
                }
            }
        }
        
        stage('Push Docker Image') {
            steps {
                script {
                    def imageName = "${REGISTRY}/${IMAGE_NAME}"
                    def imageURL = "${imageName}:${env.BUILD_NUMBER}"
                    sh "docker tag ${IMAGE_NAME}:latest ${imageURL}"
                    sh "docker tag ${IMAGE_NAME}:latest ${imageName}:latest"
                    echo "Pushing Docker image: ${imageURL}"
                    withCredentials([usernamePassword(credentialsId: REGISTRY_CREDENTIALS_ID, passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        sh "echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USERNAME} --password-stdin https://${REGISTRY}"                        
                        sh "docker push ${imageURL}"
                        sh "docker push ${imageName}:latest"
                    }
                }
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}
