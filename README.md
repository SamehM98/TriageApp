# TriageApp
A mobile application that rates the user's urgency to seek medical attention; and directs them to the nearest hospital and more.

The user is welcomed with a clickable body diagram to filter his symptoms; he can switch between that and an alphabatically ordered list. Lists are diplayed using listview and a listadapter.
Upon clicking on a symptom; the user can use a seekbar to choose the severity.

![image](https://user-images.githubusercontent.com/66166781/126067145-4ebcfd3b-08ad-41b9-8253-134109a3b6d0.png)

![image](https://user-images.githubusercontent.com/66166781/126067152-97401b04-6246-4b8f-93af-800c4747e950.png)

After choosing the symptoms; users can check their symptoms before proceeding and remove/add symptoms
The mobile application sends a request to the a recommender system hosted on Heroku, via Flask API and http requests, the mobile app sends the symptoms; in return the API responds with a list of related symptoms; displayed in a list where the user can add to their symptoms in a similar fashion to the first stage.

![image](https://user-images.githubusercontent.com/66166781/126067009-d608575a-4b47-445b-b99b-2a4026beb0a9.png)

After choosing; the user has a chance to verify again.
Finally; the user proceeds to the results screen. The mobile application sends a request to the Machine learning model with the new symptoms and their respective severity scales entered by the user. The ML model responds with a scale of 1-5; which is mapped to readable form to the user; the text and value change accordingly to the scale.

![image](https://user-images.githubusercontent.com/66166781/126067053-a8a5ba9a-e8a2-4d85-857a-15db437e2af2.png)

Finally, the user can click on the "Display nearby hospitals" to display nearby medical facilities.
Firstly; the mobile app requests location permission. If accepted, the app gets the user's location and is used as a paramater to a request to Google Maps API; which returns a list of medical facilities and are displayed on a map in red icons, as shown below.

![image](https://user-images.githubusercontent.com/66166781/126067118-ac107e94-edf3-4ebe-b9c4-1b418b35c807.png)
