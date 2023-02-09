import sys
import time
import random
from Adafruit_IO import MQTTClient
from videoDect import *

AIO_FEED_IDs = ["button1", "button2"]
AIO_USERNAME = "quang_cao2002"
# AIO_KEY = "aio_SgOD79eo2vwblPtJWStbEp8KkqXF"

def connected(client):
    print("Connected ...")
    for topic in AIO_FEED_IDs:
        client.subscribe(topic)
    # client.subscribe(AIO_FEED_IDs)

def subscribe(client , userdata , mid , granted_qos):
    print("Subscribed ...")

def disconnected(client):
    print("Disconnected ...")
    sys.exit (1)

def message(client , feed_id , payload):
    print("Value Received: " + payload + ", FeedID: " + feed_id)

client = MQTTClient(AIO_USERNAME , AIO_KEY)
client.on_connect = connected
client.on_disconnect = disconnected
client.on_message = message
client.on_subscribe = subscribe
client.connect()
client.loop_background()

counter = 10
sensor_type = 0
counter_ai = 5
ai_result = ""
previous_result = ""
while True:
    counter -= 1
    if counter <= 0:
        counter = 10
        #TODO
        print ("Random data is publishing ...")
        if sensor_type == 0:
            print ("Temperature ...")
            temp = random.randint(20, 30)
            client.publish("sensor1", temp)
        elif sensor_type == 1:
            print ("Humidity ...")
            humid = random.randint(20, 50)
            client.publish("sensor2", humid)
        elif sensor_type == 2:
            print ("Gas Concentration ...")
            gas = round(random.uniform(0, 2.5),2)
            client.publish("sensor3", gas)
        sensor_type = (sensor_type + 1) % 3
    
    counter_ai -= 1
    if counter_ai <= 0:
        counter_ai = 5
        # Print result to console
        print ("AI Result: ")
        image_detector()
        # Only publish to server in case values are different
        previous_result = ai_result
        ai_result = image_detector()
        if previous_result != ai_result:
            client.publish("ai", ai_result)
    time.sleep(1)
    pass