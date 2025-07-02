pipeline {

agent any

environment {
        DOCKER_TOKEN=credentials('docker-push-secret')
        DOCKER_USER='alexar77'
        DOCKER_SERVER='ghcr.io'
        DOCKER_PREFIX='ghcr.io/alexar77/homesharing'
    }


stages {
    stage('Test') {
        steps {
            sh '''
                echo "Start testing"
                ./mvnw test
            '''
        }
    }

    stage('Docker build and push') {
            steps {
                sh '''
                    HEAD_COMMIT=$(git rev-parse --short HEAD)
                    TAG=$HEAD_COMMIT-$BUILD_ID
                    docker build --rm -t $DOCKER_PREFIX:$TAG -t $DOCKER_PREFIX:latest -f nonroot-multistage.Dockerfile .
                '''

                sh '''
                    echo $DOCKER_TOKEN | docker login $DOCKER_SERVER -u $DOCKER_USER --password-stdin
                    docker push $DOCKER_PREFIX --all-tags
                '''
            }
        }

    stage('deploy to kubernetes') {
            steps {
                sh '''
                    HEAD_COMMIT=$(git rev-parse --short HEAD)
                    TAG=$HEAD_COMMIT-$BUILD_ID
                    export ANSIBLE_CONFIG=~/workspace/ansible-job/ansible.cfg
                    ansible-playbook -i ~/workspace/ansible-job/hosts.yaml -e new_image=$DOCKER_PREFIX:$TAG ~/workspace/ansible-job/playbooks/k8s-update-spring-deployment.yaml
                '''
            }
        }    
}
}