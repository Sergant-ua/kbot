pipeline {
    agent any

    parameters {
        choice(name: 'OS', choices: ['linux', 'darwin', 'windows', 'all'], description: 'Pick OS')
        choice(name: 'ARCH', choices: ['amd64', 'arm64', 'all'], description: 'Pick ARCH')
    }

    enviroment {
        REPO = 'https://github.com/Sergant-ua/kbot'
        BRANCH = 'develop' 
        DOCKER_REGISTRY_URL = "https://ghcr.io"
        GITHUB_USERNAME = "Sergant-ua"
        
        GITHUB_PAT = credentials('GITHUB_PAT_TOKEN')
    }

    stages {
        stage('clone') {
            steps {
                echo "Clone repository"
                git branch: "${BRANCH}", url: "${REPO}"
            }
        }

        stage('test') {
            steps {
                echo "Test Application"
                sh 'make test'
            }
        }

        stage('build') {
            steps {
                echo "Build Application"
                sh 'make build'
            }
        }

        stage('image') {
            steps {
                echo "Build Docker image"
                sh 'make image'
            }
        }

        stage('push') {
            steps {
                echo "Push Docker image to regisrty"
                docker.withRegistry(DOCKER_REGISTRY_URL, "${GITHUB_USERNAME}:${GITHUB_PAT}") {
                    sh 'make push'
                }
            }
        }

        stage('clean') {
            steps {
                echo "clean"
                sh 'make clean'
            }
        }


    }
}