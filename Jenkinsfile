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
		            def jacocoFile = 'target/site/jacoco/jacoco.xml'
		            if (!fileExists(jacocoFile)) {
		                error "JaCoCo coverage report not found!"
		            }
		
		            def report = new XmlSlurper().parse(jacocoFile)
		            def instructionCounter = report.counter.find { it.@type == 'INSTRUCTION' }
		            def lineCounter = report.counter.find { it.@type == 'LINE' }
		            def complexityCounter = report.counter.find { it.@type == 'COMPLEXITY' }
		
		            
		            def instructionMissed = instructionCounter.@missed.toInteger()
		            def instructionCovered = instructionCounter.@covered.toInteger()
		            def lineMissed = lineCounter.@missed.toInteger()
		            def lineCovered = lineCounter.@covered.toInteger()
		            def complexityMissed = complexityCounter.@missed.toInteger()
		            def complexityCovered = complexityCounter.@covered.toInteger()
		
		           
		            def instructionRate = (instructionCovered / (instructionCovered + instructionMissed)) * 100
		            def lineRate = (lineCovered / (lineCovered + lineMissed)) * 100
		            def complexityRate = (complexityCovered / (complexityCovered + complexityMissed)) * 100
		
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