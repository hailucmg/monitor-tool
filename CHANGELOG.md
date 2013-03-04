CHANGELOG (notable new features or fixes)
---------
### 4.0.1
* Released on: 04/03/2013
* Fix issue DMON-28: System detail: UI is not good If info is expand after horizontal screen
* Fix issue DMON-23: The display is not good If the error name is long
* Fix issue DMON-6: The about page display

### 4.0.0
* Released on: 22/01/2013
* Revision number ( SVN) : 712
* To compatible with mobile device's browsers: the system will automatically detect client's device is being used to response suitable pages
* To retrieve database connection pool services and monitor them

### 3.5.0
* Released on: 25/10/2012
* Revision number ( SVN) : 646
* Allows user who has Gmail account (non Google apps account) can log-in ( invite or request )
* Exclusion monitoring time: set to not monitoring in a time frame

### 3.0.0
* Released on: 10/10/2012
* Revision number ( SVN) : 582
* Move groups and roles management from Google App to inside Health Monitoring System, before this is depended on Google Apps
* Allows to synchronize user accounts from Google app to monitoring system
* Proxy link: this allows Health Monitoring Tool retrieve data from remote system via the Proxy link in case of unsigned certificate failure ( SSL )
* In Dashboard: User only sees monitoring system on dashboard if the user belongs to the group which is setup for the monitoring system, before user can see all minoring systems on the dashboard    regardless of these systems in which groups
* In Dashboard: "Real time" data showing: in system information page, some graphs now shows a "real time" data, and movement of graphs
* Email notification: improve the alignment, and wording
* Added reversion page: this allows users to see the evolution of the monitor tool

### 2.0.0
* Released on: 13/01/2012
* Revision number ( SVN) : 409
* System logs: to see who changed, and what change on System management
* Flexible Notification Options : this will be reduce number of email notification we want, before Health Monitoring System always alerts base on fixed conditions

### 1.0.0
* Released on: 25/12/2011
* Revision number ( SVN) : 350
* Initial version with main functions
* Login function: this is to integrate with Google SSO
* Monitor function: to retrieve data from remote system, analysis the data and do alert base on fixed conditions
* Dashboard: to show all monitoring system with health status, and statistic data
* Administration: allows user to manage monitoring systems