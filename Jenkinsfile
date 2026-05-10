pipeline {
    agent any
    
    environment {
        DOCKER_COMPOSE_FILE = 'docker-compose.part2.yml'
        SELENIUM_TESTS_DIR = 'RentEase-Selenium-Tests'
        PROJECT_DIR = '/home/ubuntu/RentEase'
    }
    
    stages {
        stage('Checkout Application') {
            steps {
                dir(PROJECT_DIR) {
                    git branch: 'main',
                        url: 'https://github.com/Maryam-Yaqoob/RentEase.git',
                        credentialsId: 'github-credentials'
                }
            }
        }
        
        stage('Checkout Selenium Tests') {
            steps {
                dir(SELENIUM_TESTS_DIR) {
                    git branch: 'main',
                        url: 'https://github.com/Maryam-Yaqoob/RentEase-Selenium-Tests.git',
                        credentialsId: 'github-credentials'
                }
            }
        }
        
        stage('Start Docker Containers') {
            steps {
                dir(PROJECT_DIR) {
                    script {
                        sh '''
                            # Stop and remove existing containers
                            docker compose -f ${DOCKER_COMPOSE_FILE} down -v 2>/dev/null || true
                            
                            # Start fresh containers
                            docker compose -f ${DOCKER_COMPOSE_FILE} up -d
                            
                            # Wait for services to be healthy
                            echo "Waiting for services to start..."
                            sleep 15
                            
                            # Check container status
                            docker compose -f ${DOCKER_COMPOSE_FILE} ps
                        '''
                    }
                }
            }
        }
        
        stage('Verify Services') {
            steps {
                script {
                    sh '''
                        echo "Verifying backend..."
                        curl -f http://localhost:8001/docs || exit 1
                        
                        echo "Verifying frontend..."
                        curl -f http://localhost:5173 || exit 1
                        
                        echo "All services are running!"
                    '''
                }
            }
        }
        
        stage('Fix Test Configuration') {
            steps {
                dir(SELENIUM_TESTS_DIR) {
                    script {
                        sh '''
                            # Fix port from 5174 to 5173 if needed
                            find . -type f -name "*.java" -exec sed -i 's/5174/5173/g' {} \\;
                            
                            # Fix any IP address references
                            find . -type f -name "*.java" -exec sed -i 's/localhost/13.48.132.213/g' {} \\;
                        '''
                    }
                }
            }
        }
        
        stage('Run Selenium Tests') {
            steps {
                dir(SELENIUM_TESTS_DIR) {
                    script {
                        try {
                            sh '''
                                # Clean and run tests
                                mvn clean test
                            '''
                        } catch (Exception e) {
                            currentBuild.result = 'FAILURE'
                            throw e
                        }
                    }
                }
            }
            post {
                always {
                    dir(SELENIUM_TESTS_DIR) {
                        // Archive test reports
                        junit 'target/surefire-reports/*.xml'
                        
                        // Publish HTML reports
                        publishHTML([
                            allowMissing: true,
                            alwaysLinkToLastBuild: true,
                            keepAll: true,
                            reportDir: 'target/surefire-reports',
                            reportFiles: 'index.html, emailable-report.html',
                            reportName: 'TestNG Report'
                        ])
                    }
                }
            }
        }
        
        stage('Stop Docker Containers') {
            steps {
                dir(PROJECT_DIR) {
                    script {
                        sh '''
                            docker compose -f ${DOCKER_COMPOSE_FILE} down -v
                        '''
                    }
                }
            }
        }
    }
    
    post {
        always {
            script {
                // Get commit information
                def commitAuthor = sh(script: "git log -1 --pretty=format:'%an'", returnStdout: true).trim()
                def commitEmail = sh(script: "git log -1 --pretty=format:'%ae'", returnStdout: true).trim()
                def commitMessage = sh(script: "git log -1 --pretty=format:'%s'", returnStdout: true).trim()
                def commitHash = sh(script: "git log -1 --pretty=format:'%h'", returnStdout: true).trim()
                def buildUrl = env.BUILD_URL
                def jobName = env.JOB_NAME
                def buildNumber = env.BUILD_NUMBER
                def buildStatus = currentBuild.result ?: 'SUCCESS'
                
                // Email subject based on build status
                def subject = "${buildStatus}: ${jobName} - Build #${buildNumber}"
                
                // Email body
                def body = """
                    <html>
                    <body>
                        <h2>Build ${buildStatus}</h2>
                        <p><strong>Job:</strong> ${jobName}</p>
                        <p><strong>Build Number:</strong> ${buildNumber}</p>
                        <p><strong>Build URL:</strong> <a href='${buildUrl}'>${buildUrl}</a></p>
                        <p><strong>Status:</strong> ${buildStatus}</p>
                        
                        <h3>Commit Information</h3>
                        <ul>
                            <li><strong>Author:</strong> ${commitAuthor}</li>
                            <li><strong>Email:</strong> ${commitEmail}</li>
                            <li><strong>Hash:</strong> ${commitHash}</li>
                            <li><strong>Message:</strong> ${commitMessage}</li>
                        </ul>
                        
                        <h3>Test Results</h3>
                        <p>Check the attached test reports for detailed results.</p>
                        
                        <hr>
                        <p><i>This is an automated email from Jenkins CI/CD Pipeline.</i></p>
                    </body>
                    </html>
                """
                
                // Send email based on build status
                if (buildStatus == 'SUCCESS') {
                    emailext(
                        to: 'maryamyaqub616@gmail.com',
                        subject: subject,
                        body: body,
                        mimeType: 'text/html',
                        attachmentsPattern: '**/target/surefire-reports/emailable-report.html',
                        replyTo: 'noreply@jenkins.com'
                    )
                } else if (buildStatus == 'FAILURE') {
                    emailext(
                        to: 'maryamyaqub616@gmail.com',
                        subject: subject,
                        body: body,
                        mimeType: 'text/html',
                        attachmentsPattern: '**/target/surefire-reports/*.xml',
                        replyTo: 'noreply@jenkins.com'
                    )
                }
            }
        }
        
        cleanup {
            script {
                sh '''
                    # Clean up Docker resources
                    docker system prune -f
                    
                    # Remove any remaining containers
                    docker ps -a | grep rentease | awk '{print $1}' | xargs -r docker rm -f
                    
                    # Remove volumes
                    docker volume ls | grep rentease | awk '{print $2}' | xargs -r docker volume rm -f
                '''
            }
        }
    }
}
