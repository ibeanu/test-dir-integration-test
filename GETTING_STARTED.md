# Getting Started as a Developer

## Get Access to Tools
- In Secure, request access to the Global Groups 
  - OpenShiftContainerPlatform_nonprod_users:  Access to the OpenShift Origin/Container Platform non prod environment
  - OpenShiftContainerPlatform_nonprod_users_DMZ:  Access to the OpenShift Origin/Container Platform non prod DMZ environment
  - OpenShiftContainerPlatform_prod_users:  **Restricted Use** Access to the OpenShift Container Platform prod environment
  - OpenShiftContainerPlatform_prod_users_DMZ: Access to the OpenShift Origin/Container Platform prod DMZ environment
  
# Tools and Links
- OpenShift: https://ocp-ctc-core-nonprod.optum.com
- Jenkins: https://jenkins-genomics-consent.ocp-ctc-core-nonprod.optum.com
- GitHub: https://github.optum.com/Genomix/test-dir-integration-test
- Rally: https://rally1.rallydev.com/#/205002364668d/dashboard
- Genomics England Help Desk: https://www.genomicsengland.co.uk/contact-us/
- SwaggerUI: http://162.13.50.221:8080/api/doc#!/testdirectory





# To restore Jenkins after a POD restart which loses all Jenkins Config data
- Install Jenkins Plugin:  SCM Sync Configuration (involves restart)
- Configure GitHub Enterprise Servers
  - API Endpoint: https://github.optum.com/api/v3
  - Name: https://github.optum.com/api/v3
- Configure SCM Synch Plugin<br>
    https://username:token@github.optum.com/Genomix/jenkins-config.git<br>
    -  In the GIt REPO, create a token for your username for this.
    - The first scan will fail to connect to GIT
- Register the username with the git config on the Jenkins system
  - Create a simple Freestyle Jenkins project called "Initialize04192018" using the date in the project name
  - Add a Shell step and register the username that will be used to fetch the Jenkins Config from GitHub
    <pre>
        git config --global user.email eric.starr@optum.com
        git config --global user.name estarr
    </pre>
- Restore from GitHub
  - You do this by navigating to the SCM Sync Confguration section and clicking "Reload config from SCM"
  - You may have to click "load from disk" link and possibly another permissions click through

  

