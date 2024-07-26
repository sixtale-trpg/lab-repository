pipeline {
    agent any
    tools {
        gradle 'myGradle'		
        nodejs 'NodeJS'    
    }
    environment {
        PATH = "${env.PATH}:/usr/bin"
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

        stage('SSH AGENT') {
            steps {
                sshagent(['d108-sixtale']) {
                    sh 'scp backEnd/SIXTALEBackEnd/gradle/wrapper/gradle-wrapper.jar ubuntu@i11D108.p.ssafy.io:/usr/bin'
                    sh 'ssh ubuntu@i11D108.p.ssafy.io "some-command"'
                }
            }
        }
        stage('Copy build file'){
            steps{
                dir("./backEnd/SIXTALEBackEnd"){
                    sshagent (credentials: 'd108-sixtale'){
                        sh 'echo ${TARGET_HOST}'
                        sh 'echo ${PROJECT_PATH}'
                        sh 'scp -o StrictHostKeyChecking=no Dockerfile ${TARGET_HOST}:${PROJECT_PATH}'
                        sh 'scp -o StrictHostKeyChecking=no gradle/wrapper/gradle-wrapper.jar ${TARGET_HOST}:${PROJECT_PATH}'
                    }
                }
            }
        }
        stage('Stop process'){
            steps{
                sshagent (credentials: 'd108-sixtale'){
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
                sshagent (credentials: 'd108-sixtale'){
                   sh'''
                   ssh -o StrictHostKeyChecking=no ${TARGET_HOST} "docker build --tag ${IMAGE_NAME}:${NEW_VERSION} --build-arg ARG_PROFILE=test ${PROJECT_PATH}"
                   ssh -o StrictHostKeyChecking=no ${TARGET_HOST} "docker run -v server-file-test:/file --name ${CONTAINER_NAME} -p 8888:8888 -d ${IMAGE_NAME}:${NEW_VERSION}"
                   '''
                }
            }
        }
    }
}
