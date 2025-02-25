curl -X POST "http://localhost:8081/Medifli/favorites/1?idContent=1&contentType=2"

curl -X POST "http://localhost:8081/Medifli/favorites/1?idContent=1&contentType=2"

curl -X POST "http://localhost:8081/Medifli/favorites/2?idContent=2&contentType=2"

curl -X POST "http://localhost:8081/Medifli/favorites/3?idContent=3&contentType=2"



# Elimina un favorito
curl -X DELETE "http://localhost:8081/Medifli/favorites/1/7"


curl -X GET "http://localhost:8081/Medifli/favorites/1"

