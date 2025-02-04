pipeline {
    agent any
    tools {
        maven 'Maven-3.9.9' 
    }
    
    stages {
        stage('Compile') {
            steps {
                script {
                    echo 'Compiling the project...'
                    sh 'echo "PATH: $PATH"'
	                sh 'which mvn || echo "Maven not found!"'
	                sh 'mvn -version || echo "Maven command failed!"'
                    sh 'mvn clean compile'
                }
            }
        }
        
        stage('Build') {
            steps {
                script {
                    echo 'Reading properties from application.properties and building the project...'
                    echo "Binding variables: ${binding.variables}"
                    sh 'mvn clean install'
                }
            }
        }
        
    	stage('Test and Coverage') {
		    steps {
				sh '''mvn clean test jacoco:report'''
		        script {
				   def jacocoFile = "${env.WORKSPACE}/target/site/jacoco/jacoco.xml"
        		   echo "JaCoCo report path: ${jacocoFile}"
                   
		             if (!fileExists(jacocoFile)) {
                		error "JaCoCo coverage report not found at ${jacocoFile}!"
            		}
                    
                    def metrics = parseJacocoReport(jacocoFile)
                    echo "XML file is exracted..."
		           
                    def instructionRate = (metrics.instructionCovered / (metrics.instructionCovered + metrics.instructionMissed)) * 100
                    def lineRate = (metrics.lineCovered / (metrics.lineCovered + metrics.lineMissed)) * 100
                    def complexityRate = (metrics.complexityCovered / (metrics.complexityCovered + metrics.complexityMissed)) * 100
                   
                    echo "Instruction Coverage: ${instructionRate.round(2)}%"
                    echo "Line Coverage: ${lineRate.round(2)}%"
                    echo "Complexity Coverage: ${complexityRate.round(2)}%"

                    def coverageThreshold = 60.0
                    if (instructionRate < coverageThreshold || lineRate < coverageThreshold || complexityRate < coverageThreshold) {
                        currentBuild.result = 'UNSTABLE'
                        echo "Build marked as UNSTABLE due to low test coverage."
                    }
                    
		        }
		    }
		}
    }
}


@NonCPS
def parseJacocoReport(String jacocoFile) {
    def xmlParser = new XmlSlurper(false, false) 
    xmlParser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)
    xmlParser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
    def report = xmlParser.parse(jacocoFile)
    
    def instructionCounter = report.counter.find { it.@type == 'INSTRUCTION' }
    def lineCounter = report.counter.find { it.@type == 'LINE' }
    def complexityCounter = report.counter.find { it.@type == 'COMPLEXITY' }

    return [
        instructionMissed: instructionCounter.@missed.toInteger(),
        instructionCovered: instructionCounter.@covered.toInteger(),
        lineMissed: lineCounter.@missed.toInteger(),
        lineCovered: lineCounter.@covered.toInteger(),
        complexityMissed: complexityCounter.@missed.toInteger(),
        complexityCovered: complexityCounter.@covered.toInteger()
    ]
}
