<h1> Cisco Demo Project </h1>
<b> Setup instructions for local use </b>
<ol>

<li>
 postgresql installation, setup
 <ul>
    <li>sudo apt-get install postgresql (install)</li>
    <li>sudo service postgresql restart (restart service)</li>
 </ul>
</li>
<li>
 postgresql user setup
 <ul>
     <li>sudo -u postgres createuser --interactive --password user12 (create new database user, in this example I chose user12 as the username)</li>
        
        Shall the new role be a superuser? (y/n) n
        Shall the new role be allowed to create databases? (y/n) y
        Shall the new role be allowed to create more new roles? (y/n) n
 </ul>
</li>
<li>
 postgresql change authentication method for unix domain socket to trust local connections, then restart server
 <ul>
     <li>sudo vi /etc/postgresql/9.5/main/pg_hba.conf</li>
     
     # "local" is for Unix domain socket connections only
     local   all             all                                     trust
     # IPv4 local connections:
     host    all             all             127.0.0.1/32            trust
 </ul>
</li>
<li>Update src/main/resources/application.properties file to include new username, password, and url to database.</li>
<li>Run main method in src/main/java/com.connollyproject.demo/DemoApplication.java</li>
</ol>

<div>
<b>Hosted on</b>: 
https://murmuring-lowlands-95005.herokuapp.com <br>

*** Calling Heroku Endpoints may be slow at first *** <br>

<b>Api Endpoints</b>: <br>
https://murmuring-lowlands-95005.herokuapp.com/api/objects/{id} (GET) <br>
https://murmuring-lowlands-95005.herokuapp.com/api/objects/{id} (PUT) <br>
https://murmuring-lowlands-95005.herokuapp.com/api/objects/{id} (DELETE) <br>
https://murmuring-lowlands-95005.herokuapp.com/api/objects (GET) <br>
https://murmuring-lowlands-95005.herokuapp.com/api/objects (POST) <br>
</div>