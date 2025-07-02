pipeline {
    agent any

    parameters {
        booleanParam(name: 'INSTALL_POSTGRES', defaultValue: true, description: 'Install PostgreSQL')
        booleanParam(name: 'INSTALL_SPRING', defaultValue: true, description: 'Install Spring Boot app')
    }

    stages {
        stage('test connection to deploy env') {
        steps {
            sh '''
                ansible -i ~/workspace/ansible-job/hosts.yaml -m ping dbserver-vm,appserver-vm
            '''
            }
        }
        
        stage('Install postgres') {
             when {
                expression { return params.INSTALL_POSTGRES }
            }
            steps {
                sh '''
                    export ANSIBLE_CONFIG=~/workspace/ansible-job/ansible.cfg
                    ansible-playbook -i ~/workspace/ansible-job/hosts.yaml -l dbserver-vm ~/workspace/ansible-job/playbooks/postgres.yaml
                '''
            }
        }

        stage('install springboot') {
             when {
                expression { return params.INSTALL_SPRING }
            }
            steps {
                sh '''
                    export ANSIBLE_CONFIG=~/workspace/ansible-job/ansible.cfg
                    ansible-playbook -i ~/workspace/ansible-job/hosts.yaml -l appserver-vm ~/workspace/ansible-job/playbooks/spring.yaml
                '''
            }
        }
}
}