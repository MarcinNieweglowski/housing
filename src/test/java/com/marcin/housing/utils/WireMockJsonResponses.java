package com.marcin.housing.utils;

public class WireMockJsonResponses {

    public static final String HOUSING_CLIENT_FETCH_HOUSING_DATA_RESPONSE = """
            {
              "totalPages": 1,
              "data": [
                {
                  "id": "d8834ae1-8129-4a7d-8ecc-e8b4b53531e9",
                  "type": "detached_house",
                  "price": "760332.99",
                  "description": "A really nice house you should buy it",
                  "area": "120",
                  "rooms": 4
                },
                {
                  "id": "e2145cc8-730f-40e2-a7a0-f90816eae55c",
                  "type": "flat",
                  "price": "490534",
                  "description": "Live your dreams at the heart of Katowice",
                  "area": "44.5",
                  "rooms": 2
                },
                {
                  "id": "5d301374-f8f8-48c7-ac79-4e8e68659b39",
                  "type": "flat",
                  "price": "540534",
                  "description": "A space for you and your universe",
                  "area": "70.34",
                  "rooms": 3
                },
                {
                  "id": "4caec08d-3cb7-46e0-9883-691eae200584",
                  "type": "semi_detached_house",
                  "price": "778098",
                  "description": "Your new place on earth",
                  "area": "150",
                  "rooms": 3
                },
                {
                  "id": "0db65236-dfb0-4d70-990f-63411af8213c",
                  "type": "terraced_house",
                  "price": "431098",
                  "description": "The greenest area in Poland for you and your family",
                  "area": "90",
                  "rooms": 4
                }
              ]
            }
            """;

    public static final String HOUSING_CLIENT_FETCH_HOUSING_DATA_EMPTY_RESPONSE = """
            {
                "totalPages": 0,
                "data": []
            }
            """;
}
