# Warm Up My House
Uses Google Maps to determine time to home and pre-heat/cool my smart home.  This uses a custom Hubitat device driver and a tasker task/scene to accomplish the integration.

# Components used:
1. Google Maps API
2. Custom Device handler
3. MakerAPI for Hubitat
4. Tasker Task and Scene


# Flow of the integration
1. Tasker
	* calls Google API to get time to home (in seconds)
	* calls device exposed via remote end point in MakerAPI with time to home (in seconds)
2. Hubitat device receives time to home in seconds and based on configuration will either:
	* turn on the custom switch device immediately
	* or schedule the custom switch device to turn on after a delay (which is configurable)
3. The custom Hubitat device will auto turn off after a configurable time (default is 5 seconds).

# Customization:
The basics of the integration are all here.  You are free to configure the tasker side as long as you can trigger a task it'll work.  On the hubitat side, the custom switch device is turned on for 5 seconds (configurable) and then turns off.  You'll need to use the custom device in a rule or an App (I'm using the Ecobee Suite Manager to accomplish what you want.

# Setup
## Google Maps API key
You'll need to generate a Google Maps API key to use in tasker.
Directions to setup a key can be found here: https://developers.google.com/maps/documentation/embed/get-api-key.
You are required to add a credit card, but there are disclaimers mentioning that you will not be charged until you specifically enable billing.  Grab your API key for later use in Tasker.

## Custom Device handler
1. Install the custom device driver (in the folder Hubitat-device-code)
2. Create a new device using the new device driver.  Note the device number in the url, it will be used in tasker.  (Example: http://[Hubitat IP Address]/device/edit/[DeviceID])

## MakerAPI for Hubitat
1. Install and setup MakerAPI for Hubitat
2. External access is required, so enable the slider called "Allow Access via Remote/Cloud".  This will also add a token to the cloud URLs, the token is also needed for Tasker.
3. Note the AppID in the URL when in the MakerAPI app, it will be used in tasker.  installedapp/configure/[AppID]/mainPage)
4. Be sure to add the device you created previously to MakerAPI.

## Tasker
1. Download the task and scene XML files from this github repository to your phone and import it into tasker.  To import: long press the Tasks or Scene tab in the Tasker app and import.
2.  Tasker will need several variables to be set, they are as follows:
* WUMH_Access_Token = MakerAPI Access Token
* WUMH_AppID = MakerAPI AppID
* WUMH_DeviceID = Device ID of the device setup using the custom driver
* WUMH_GMapsApiKey = Google Maps API Key (you need to sign up for this)
* WUMH_HOME = You Home's location (I'm using a zip code, but you can use coordinates).  Review the Google Maps API documentaion for futher inoformaion).
* WUMH_Hubitat_Cloud_Token = MakerAPI Token added to Hubitat Cloud URLs, this should appear in the MakerAPI after you enable for remote/cloud access). 

# Reference URLs:
URL used to get Google Maps data:
https://maps.googleapis.com/maps/api/directions/json?origin=[ORIGIN LOCATION]&destination=[DESTINATION LOCATION]&key=[GOOGLE MAPS API KEY]
The two locations are defined in the Google documentation, but it may be zip code or coordintes (this is what us returned from tasker for the users current location). 

Hubitat Cloud URL used to send time to Hubitat and schedule a device to turn on:
https://cloud.hubitat.com/api/[Hubitat_Cloud_Token]/apps/[AppID]/devices/[DeviceID]/setlevel/[Seconds_determined_by_tasker]?access_token=[Access_Token]
