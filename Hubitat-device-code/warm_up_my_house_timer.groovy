/**
 *  Warm Up My House (Timer)
 *
 *  Purpose is to create a virtual switch device handler that will allow for input of a delay in seconds to turn on the device.
 *  It reuses the "value" from the setLevel() command to schedule a delay in seconds.
 *
 *  Configurable settings:
 *  Delay (in seconds) The switch will turn off automatically (using a configurable setting).
 *	Whether the switch will turn on immediately or after a delay.  The delay time is also configurable.
 *
 * Revision History:
 * 2019-12-30 - Initial commit
 *
 */

metadata {
    definition (name: "Warm Up My House (Timer)", namespace: "shadowjig", author: "shadowjig") {
	    capability "Switch"
	    capability "Switch Level"
	}

preferences {
	input description: "Setting Delay Type to Immediate will ignore the input value and turn on this switch immediately.  Setting Delay Type to Offset will subtract the Offset Minutes from the input value, therefore delaying this switch from turning on.", title: "Usage", displayDuringSetup: false, type: "paragraph", element: "paragraph"
	input(name: "delayType", , type:"enum", title: "Delay Type", required: true, options: ['Immediate', 'Offset'])
	input(name: "offsetMins", type: "number", title:"Offset Minutes (default: 20 minutes)", required: true, defaultValue: 20)
	input(name: "resetSecs", type: "number", title:"This switch will auto turn off.  This setting is the delay time in seconds (default: 5 seconds).", required: true, defaultValue: 5)
	}
}

def on() {
	state.Seconds_Delay_Until_Off = resetSecs as int
	runIn(state.Seconds_Delay_Until_Off, off,[overwrite: true])
	sendEvent(name: "switch", value: "on")
}

def off() {
	unschedule()
	sendEvent(name: "switch", value: "off")
}

//Reuse setLevel() as a way to input a delayed ON event.  Duration is in seconds, value is not used.
def setLevel(value) {
	// Check for positive value
	if (value > 0) {
		unschedule()
		if ((delayType == 'Offset') && (value > (offsetMins * 60))) {
			state.Seconds_Delay_Until_On = value - (offsetMins * 60)			
			runIn(state.Seconds_Delay_Until_On, on, [overwrite: true])
		} else { on() }
	} else {
		log.debug "value was not specified in setLevel()"
	}	
}
