pipeline {
    agent any
    tools {
        gradle 'myGradle'		
        nodejs 'NodeJS'    
    }
    environment {
        TARGET_HOST = 'ubuntu@i11D108.p.ssafy.io'
        PATH = "${env.PATH}:/usr/bin"
        IMAGE_NAME = 'sixtale-test'
        NEW_VERSION = '1.0.0'
        CONTAINER_NAME = 'jenkins-test'
        PROJECT_PATH = '/sixtale-test'
    }
    stages {
        stage('Clone') {
            steps {
                git branch: 'master', credentialsId: 'd108-sixtale', url: 'https://lab.ssafy.com/s11-webmobile1-sub2/S11P12D108'
            }
        }
        stage('BE-Build') {
            steps {
                dir("./backEnd/SIXTALEBackEnd"){
                    sh 'chmod +x gradlew'
                    sh './gradlew clean build'
                }
            }
        }
        stage('FE-Build') {
            steps {
                dir("./frontEnd"){
                    withEnv(['NODE_OPTIONS=--openssl-legacy-provider']) {
                        script {
                            sh 'npm install'
                            sh 'npm run build'
                        }
                    }
                }   
            }
        }
        stage('SSH Agent') {
            steps {
                sshagent(['ssh-server']) {
                    sh 'scp -o StrictHostKeyChecking=no backEnd/SIXTALEBackEnd/gradle/wrapper/gradle-wrapper.jar ubuntu@i11D108.p.ssafy.io:/home/ubuntu/'
                    sh 'ssh ubuntu@i11D108.p.ssafy.io "sudo mv /home/ubuntu/gradle-wrapper.jar /usr/bin/"'
                }
            }
        }
        stage('Copy build file'){
            steps{
                dir("."){
                    sshagent (['ssh-server']){
                        sh 'echo ${TARGET_HOST}'
                        sh 'echo ${PROJECT_PATH}'
                        sh 'scp -o StrictHostKeyChecking=no Dockerfile ${TARGET_HOST}:/home/ubuntu/'
                sh 'scp -o StrictHostKeyChecking=no backEnd/SIXTALEBackEnd/gradle/wrapper/gradle-wrapper.jar ${TARGET_HOST}:/home/ubuntu/'
                sh 'ssh -o StrictHostKeyChecking=no ${TARGET_HOST} "sudo mv /home/ubuntu/Dockerfile /sixtale-test/"'
                sh 'ssh -o StrictHostKeyChecking=no ${TARGET_HOST} "sudo mv /home/ubuntu/gradle-wrapper.jar /sixtale-test/"'
                    }
                }
            }
        }
        stage('Stop process'){
            steps{
                sshagent (['ssh-server']){
                    script{
                        try{
                            sh'''
                             ssh -o StrictHostKeyChecking=no ${TARGET_HOST} "docker rm -f ${CONTAINER_NAME}"
                             ssh -o StrictHostKeyChecking=no ${TARGET_HOST} "docker rmi ${IMAGE_NAME}:${LAST_VERSION}"
                             '''
                        } catch (e){
                            echo 'Remove Container Failed : ${CONTAINER_NAME}'
                        }
                    }

                }
            }
        }
        stage('Deploy'){
            steps{
                sshagent (['ssh-server']){
                   sh'''
                    ssh -o StrictHostKeyChecking=no ${TARGET_HOST} "docker build --tag ${IMAGE_NAME}:${NEW_VERSION} --build-arg ARG_PROFILE=test ${PROJECT_PATH}"
                    ssh -o StrictHostKeyChecking=no ${TARGET_HOST} "docker run -v server-file-test:/file --name ${CONTAINER_NAME} -p 8888:8888 -d ${IMAGE_NAME}:${NEW_VERSION}"
                   '''
                }
            }
        }
    }
}
