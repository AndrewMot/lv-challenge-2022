**Create**
----
  Registers a call center in the platform.

* **URL**

  /call-centers

* **Method:**

  `POST`
  
*  **URL Params**

    None

* **Body**
 
  ```
  {
    name: <string>, // required
    countryName: <string>, // required
  }
  ```

* **Success Response:**

  * **Code:** 201 CREATED <br />
    **Content:** `{ id : 12, name : "NY Sales", countryName: "United States" }`
 
* **Error Response:**

  * **Code:** 400 BAD REQUEST <br />
    **Content:** `{ error : "Name is required" }`
  
  * **Code:** 400 BAD REQUEST <br />
    **Content:** `{ error : "Country name is required" }`

  * **Code:** 409 CONFLICT <br />
    **Content:** `{ error : "Name must be unique per country" }`

* **Sample Call:**

  ```javascript
    fetch("/call-centers", {
      method: "POST",
      headers: { "Content-type": "application/json", "Accept": "application/json" },
      body: JSON.stringify({ name: "NY Sales", countryName: "United States" })
    });
  ```
----

**Retrieve**
----
Retrieves a call center from the platform.

* **URL**

  /call-centers/{id}

* **Method:**

  `GET`

*  **URL Params**

   **Required:**

   `id=[integer]`

* **Body**

   None

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `{ id : 12, name : "NY Sales", countryName: "United States" }`

* **Error Response:**

  * **Code:** 404 NOT FOUND <br />
    **Content:** `{ error : "Call center not found" }`

* **Sample Call:**

  ```javascript
    fetch("/call-centers/12", {
      method: "GET",
      headers: { "Accept": "application/json" }
    });
  ```
----

**Update**
----
Updates a call center in the platform. To date, only the `name` column can be updated.

* **URL**

  /call-centers/{id}

* **Method:**

  `PUT`

* **URL Params**

  **Required:**

  `id=[integer]`

* **Body**

  ```
  {
    name: <string>, // required
  }
  ```

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `{ id : 12, name : "NY Support", countryName: "United States" }`

* **Error Response:**

  * **Code:** 400 BAD REQUEST <br />
    **Content:** `{ error : "Name is required" }`

  * **Code:** 404 NOT FOUND <br />
    **Content:** `{ error : "Call center not found" }`

  * **Code:** 409 CONFLICT <br />
    **Content:** `{ error : "Name must be unique per country" }`

* **Sample Call:**

  ```javascript
    fetch("/call-centers/12", {
      method: "PUT",
      headers: { "Content-type": "application/json", "Accept": "application/json" },
      body: JSON.stringify({ name: "NY Support" })
    });
  ```
----

**List**
----
Retrieves all call centers in the platform. Results should be paginated, you can use a different set of properties than the ones shown in the response below. 

* **URL**

  /call-centers

* **Method:**

  `GET`

* **URL Params**

  **Optional:**

  `page=[integer]`

  `pageSize=[integer]`

  `sort=["asc"|"desc"]`

* **Body**

   None

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** 

  ```javascript
    { 
      "content": [ 
        { "id" : 12, "name" : "NY Support", "countryName": "United States" },
        { "id" : 13, "name" : "Medellín Ventas", "countryName": "Colombia" },
        { "id" : 14, "name" : "Buenos Aires Soporte", "countryName": "Argentina" },
        ... 
      ],
      "total": 10, // total number of call centers
      "page": 0, // current page index
      "pageSize": 5 // number of call centers per page
    }
  ``
* ****Error Response:**

   None

* **Sample Call:**

  ```javascript
    fetch("/call-centers", {
      method: "GET",
      headers: { "Accept": "application/json" }
    });
  ```