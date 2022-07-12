### `* Rest of APIs intentionally omitted *`

----
**Assign**
----
  Assigns an agent to a call center in the platform. 

* **URL**

  /agents/{id}/assign

* **Method:**

  `PATCH`

*  **URL Params**

   **Required:**

   `id=[integer]`

* **Body**
 
  ```
  {
    callCenterId: <integer>, // required
  }
  ```

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `{ id : 99, firstName : "Jhon", lastName: "Doe", phone: "4157778899", callCenterId: 789 }`
 
* **Error Response:**

  * **Code:** 400 BAD REQUEST <br />
    **Content:** `{ error : "Call center id is required" }`

  * **Code:** 400 BAD REQUEST <br />
    **Content:** `{ error : "Call center doesn't exist" }`

  * **Code:** 409 CONFLICT <br />
    **Content:** `{ error : "An agent with same extension is already assigned to call center" }`

  * **Code:** 404 NOT FOUND <br />
    **Content:** `{ error : "Agent not found" }`

* **Sample Call:**

  ```javascript
    fetch("/agents/12/assign", {
      method: "PATCH",
      headers: { "Content-type": "application/json", "Accept": "application/json" },
      body: JSON.stringify({ callCenterId: 789 })
    });
  ```
