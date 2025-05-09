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
                   
                    def instructionRate = (metrics.instructionCovered / (metrics.instructionCovered + metrics.instructionMissed)) * 100
                    def lineRate = (metrics.lineCovered / (metrics.lineCovered + metrics.lineMissed)) * 100
                    def complexityRate = (metrics.complexityCovered / (metrics.complexityCovered + metrics.complexityMissed)) * 100
                   
                    echo "Instruction Coverage: ${instructionRate}%"
                    echo "Line Coverage: ${lineRate}%"
                    echo "Complexity Coverage: ${complexityRate}%"

                    def coverageThreshold = 49
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
     println "Parsing Jacoco report: ${jacocoFile}"
    def xmlParser = new XmlSlurper(false, false) 
    xmlParser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)
    xmlParser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        
    def report = xmlParser.parse(new File(jacocoFile)) 
	println("Parsed XML structure: " + groovy.xml.XmlUtil.serialize(report))
    println("Available fields: " + report.children().collect { it.name() })
    
	
	def totalInstructionMissed = 0
    def totalInstructionCovered = 0
    def totalLineMissed = 0
    def totalLineCovered = 0
    def totalComplexityMissed = 0
    def totalComplexityCovered = 0

  report.'counter'.each { counter ->
        def type = counter.getProperty('@type')?.toString() ?: 'type not found'
        def missed = counter.getProperty('@missed')?.toInteger() ?: 0
        def covered = counter.getProperty('@covered')?.toInteger() ?: 0
        
        println "[LOG] Processing Counter - Type: ${type}, Missed: ${missed}, Covered: ${covered}"
        
        switch (type) {
            case 'INSTRUCTION':
                totalInstructionMissed += missed
                totalInstructionCovered += covered
                break
            case 'LINE':
                totalLineMissed += missed
                totalLineCovered += covered
                break
            case 'COMPLEXITY':
                totalComplexityMissed += missed
                totalComplexityCovered += covered
                break
        }
    }

    println "Total Coverage Summary:"
    println "  Instruction - Missed: ${totalInstructionMissed}, Covered: ${totalInstructionCovered}"
    println "  Line - Missed: ${totalLineMissed}, Covered: ${totalLineCovered}"
    println "  Complexity - Missed: ${totalComplexityMissed}, Covered: ${totalComplexityCovered}"

    return [
        instructionMissed: totalInstructionMissed,
        instructionCovered: totalInstructionCovered,
        lineMissed: totalLineMissed,
        lineCovered: totalLineCovered,
        complexityMissed: totalComplexityMissed,
        complexityCovered: totalComplexityCovered
    ]
    
}
