# Backend

backend RMI, datalayer and rabbitMQ

starting rabbitMQ as docker service: docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
access from http://localhost:15672/#/

postman createbooking:

json body:
{
    "roomNumbers": ["223M"],
    "passportNumber": "123cjohn3",
    "numberOfGuests": 5,
    "arrival": "2020-12-12",
    "departure": "2021-07-01",
    "lateArrival": false
}
url: http://localhost:9091/booking 
method: POST
header: Content-Type 
value: application/json