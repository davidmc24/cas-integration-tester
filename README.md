# Server setup

* `git clone https://github.com/apereo/cas-gradle-overlay-template.git`
* `git checkout 5.1`
* `./gradlew bootRun`
* Open a browser to [here](https://localhost:8443/cas/login).
* Enter username "casuser", password "Mellon", and click "Login".
* If you got to a "Log In Successful" message, you're all set.

# Client setup
* `git clone https://github.com/davidmc24/cas-integration-tester.git`
* `./gradlew run`

# Testing authentication
* Open a browser to [here](http://localhost:8080/)
* Click "login".
* If prompted, enter username "casuser", password "Mellon", and click "Login".
* You should be presented with a page showing the user's attributes.
