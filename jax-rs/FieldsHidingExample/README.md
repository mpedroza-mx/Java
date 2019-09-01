# Fields Hiding Example

### Prerequisites
* JDK 1.8.0_201
* WildFly Full 13.0.0.Final

***
This project shows an example of how can you select the number of fields displayed in a REST service response.

The REST service manages 3 entities:

* Continent 
* Country
* State

The service only exposes the following endpoints

| HTTP METHOD  | URL                                                   | 
| ------------ | ----------------------------------------------------- | 
| GET          | http://localhost:8080/fieldsHidingExample/api/v1/continents   | 
| GET          | http://localhost:8080/fieldsHidingExample/api/v1/continents/1 | 


Now that we know the endpoints let's see an example of the full response of the first endpoint.

**Endpoint: http://localhost:8080/fieldsHidingExample/api/v1/continents**

**Service Response:**
```javascript
[
    {
        "id": 1,
        "name": "America",
        "population": null,
        "area": 42000000,
        "languages": [
            "Spanish",
            "English",
            "Portuguese",
            "French",
            "Italian"
        ],
        "countries": [
            {
                "id": 1,
                "name": "Mexico",
                "capital": "Mexico City",
                "currency": "Mexican Peso",
                "languages": [
                    "Spanish"
                ],
                "states": [
                    {
                        "id": 1,
                        "name": "Jalisco",
                        "density": "110/km",
                        "demonym": "Jalisciense"
                    },
                    {
                        "id": 2,
                        "name": "Yucatan",
                        "density": "53/km",
                        "demonym": "Yucateco"
                    }
                ]
            },
            {
                "id": 2,
                "name": "United States of America",
                "capital": "Washington DC",
                "currency": "American Dollar",
                "languages": [
                    "English"
                ],
                "states": null
            },
            {
                "id": 3,
                "name": "Canada",
                "capital": "Ottawa",
                "currency": "Canadian Dollar",
                "languages": [
                    "French"
                ],
                "states": null
            }
        ]
    },
    {
        "id": 2,
        "name": "Europe",
        "population": null,
        "area": 12000000,
        "languages": [
            "Spanish",
            "English",
            "Portuguese",
            "French",
            "Italian"
        ],
        "countries": [
            {
                "id": 4,
                "name": "Spain",
                "capital": "Madrid",
                "currency": "Euro",
                "languages": [
                    "Spanish"
                ],
                "states": null
            },
            {
                "id": 4,
                "name": "Italy",
                "capital": "Rome",
                "currency": "Euro",
                "languages": [
                    "Italian"
                ],
                "states": null
            }
        ]
    }
]
```
 
Now from this main response we are going to check some filters that we can apply to the fields displayed in the response

***
## Select fields from Continent
**Endpoint: http://localhost:8080/fieldsHidingExample/api/v1/continents?fields=id,name**

**Service Response:**

```javascript
[
    {
        "id": 1,
        "name": "America"
    },
    {
        "id": 2,
        "name": "Europe"
    }
]
```
***

## Select fields from Country
**Endpoint: http://localhost:8080/fieldsHidingExample/api/v1/continents?fields=id,countries.name**

**Service Response**

```javascript
[
    {
        "id": 1,
        "countries": [
            {
                "name": "Mexico"
            },
            {
                "name": "United States of America"
            },
            {
                "name": "Canada"
            }
        ]
    },
    {
        "id": 2,
        "countries": [
            {
                "name": "Spain"
            },
            {
                "name": "Italy"
            }
        ]
    }
]
```
***

## Select fields from States
**Endpoint: http://localhost:8080/fieldsHidingExample/api/v1/continents?fields=id,countries.name,countries.states.name**

**Service Response**

```javascript
[
    {
        "id": 1,
        "countries": [
            {
                "name": "Mexico",
                "states": [
                    {
                        "name": "Jalisco"
                    },
                    {
                        "name": "Yucatan"
                    }
                ]
            },
            {
                "name": "United States of America",
                "states": null
            },
            {
                "name": "Canada",
                "states": null
            }
        ]
    },
    {
        "id": 2,
        "countries": [
            {
                "name": "Spain",
                "states": null
            },
            {
                "name": "Italy",
                "states": null
            }
        ]
    }
]
```

And that's all I hope this may be useful for your current project.