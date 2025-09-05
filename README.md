# Prescription Management System

**Prescription Management System** is a simple web-based application for generating, managing and reporting medical prescriptions. It allows authenticated users to create, edit, delete, and view prescriptions in a secure environment. The system also provides REST APIs for integration and supports day-wise prescription reports.

---

## Features

* User authentication (no anonymous access)
  
     * **Have pre-stored user credentials data in data.sql, can login using that credential**
     * **User Authentication:** Only registered users can log in; no anonymous access.
     * **Pre-stored Credentials:** Users in data.sql can log in immediately.
     * **Controlled Signup:** New users must be approved by an database administrator (ENABLED = TRUE) before they can log in.

       (Persistent Storage: Uses a file-based H2 database to retain user data across application restarts. if you want to use it just comment out code in  applications.properties & data.sql)
       
       <img width="1571" height="662" alt="image" src="https://github.com/user-attachments/assets/9f9289ca-02d3-41b6-a254-7b27a9d768f4" />

        <img width="1613" height="667" alt="image" src="https://github.com/user-attachments/assets/f958d95c-b876-4297-b221-72a417e1261d" />

       <img width="446" height="461" alt="image" src="https://github.com/user-attachments/assets/af5a58ed-34fc-49fd-98e2-8ce26349834e" />

    * **Make Enable option as TRUE to confirming account creation**

      <img width="500" height="217" alt="image" src="https://github.com/user-attachments/assets/969b0cea-9b16-41b8-ac89-d9a89f12d713" />



     * **Can add,modify,delete user credentials from database**
       
       <img width="987" height="532" alt="1 2 db user credential" src="https://github.com/user-attachments/assets/705025cb-b8af-474b-96fa-0b1d3d6e4573" />

     * **Have option to make password encrypted so that database adminestretor can't see real password**

*Allow the authenticated users to create new prescriptions which will be saved in DB.
Also should show proper error messages when invalid data is submitted on
form submission. The prescription contain:
○ Prescription Date (valid date, mandatory)
○ Patient Name (text, mandatory)
○ Patient Age (integer, valid age range, mandatory)
○ Patient Gender (select box, mandatory)
○ Diagnosis (text area)
○ Medicines (Text area)
○ Next visit date (valid date, optional)

* Create, edit, delete prescriptions
* Day-wise prescription report
* REST API endpoint: `/api/v1/prescriptions` (JSON response)
* Role-based access control:

  * **Doctor**: create, edit, delete prescriptions.
  * **But one Doctor can't see or modify prescriptions of other Doctors.**
    
    <img width="1000" height="886" alt="image" src="https://github.com/user-attachments/assets/b1a2a645-30ea-49ed-b457-5dcc1ee3cd6c" />
    <img width="1147" height="408" alt="image" src="https://github.com/user-attachments/assets/cd89757b-f0b1-4042-a39a-17956d3aaf22" />

  * **Pharmacist/Medical Staff/Admin**: view all prescriptions
     <img width="1901" height="891" alt="image" src="https://github.com/user-attachments/assets/5301c7cc-5e5d-42f0-8da5-9318b4f2f2cf" />

* Swagger API documentation
* 
     <img width="1027" height="667" alt="image" src="https://github.com/user-attachments/assets/1aab042e-aa8a-468b-9556-127a57435467" />

  
* Responsive UI with form validation
* Integration with RxNav API for drug interactions

  <img width="1626" height="352" alt="image" src="https://github.com/user-attachments/assets/48fe7df0-d3cc-48ac-85a7-be48586adfe7" />

  <img width="1729" height="451" alt="image" src="https://github.com/user-attachments/assets/f7e3d7b5-a534-476c-9ebe-fdcb7226fed2" />


---

## Prescription Entity

Each prescription contains:

| Field             | Type      | Mandatory |
| ----------------- | --------- | --------- |
| Prescription Date | Date      | Yes       |
| Patient Name      | Text      | Yes       |
| Patient Age       | Integer   | Yes       |
| Patient Gender    | String    | Yes       |
| Diagnosis         | Text Area | No        |
| Medicines         | Text Area | No        |
| Next Visit Date   | Date      | No        |
| Doctor Name       | Text      | Yes       |

---

## Technology Stack

* **Backend:** Java, Spring Boot, Spring Security, Spring Data JPA
* **Database:** H2 (in-memory)
* **Frontend:** HTML, CSS, JavaScript
* **API Documentation:** Swagger

---

## Getting Started

### Prerequisites

* Java 17+ or compatible
* Maven or Gradle
* IDE (IntelliJ, Eclipse, VS Code)
* Optional: Postman for API testing

### How to Run

1. **Clone the Repository:**

```bash
git clone https://github.com/yourusername/healthcare.git
cd healthcare
```

2. **Build the Project (Maven):**

```bash
mvn clean install
```

3. **Run the Application:**

```bash
mvn spring-boot:run
```

4. **Access the Application:**

* Browser: `http://localhost:8080`
* H2 Console (optional): `http://localhost:8080/h2-console`



## Using Swagger for API Documentation

1. **Open Swagger UI:**

   After running your Spring Boot application, open a browser and go to:

   ```
   http://localhost:8080/swagger-ui/index.html
   ```

2. **Explore API Endpoints:**

   * You’ll see all available REST endpoints, grouped by controller.
   * Each endpoint shows:

     * HTTP method (GET, POST, PUT, DELETE)
     * URL path
     * Description (if provided via annotations)
     * Required parameters (query, path, or request body)
     * Response schema

3. **Try Out Endpoints:**

   * Click on an endpoint to expand it.
   * Click **“Try it out”**.
   * Fill in the required parameters or request body in JSON format.
   * Click **“Execute”**.
   * You’ll see:

     * **Request URL**
     * **Request headers**
     * **Request body**
     * **Response status code**
     * **Response body**

4. **View API Response:**

   * Swagger displays the response returned by your API in real-time.
   * Example: `/api/v1/prescriptions` will show the list of prescriptions in JSON format.

5. **Download OpenAPI Spec (Optional):**

   * Swagger UI generates a machine-readable OpenAPI JSON spec.
   * You can access it at:

     ```
     http://localhost:8080/v3/api-docs
     ```

6. **Notes:**

   * Make sure your Spring Boot application is running.
   * If you change or add endpoints, Swagger UI automatically reflects the updates.
   * Use Swagger for testing APIs before integrating with frontend.

---


2. **Explore APIs:**

* View HTTP methods (GET, POST, PUT, DELETE)
* Check request parameters and body
* See example responses

3. **Test APIs:**

* Click **“Try it out”**
* Enter request body / parameters
* Click **Execute**
* Check server response and status code

---

## API Endpoints

### 1. Signup

**POST /api/v1/auth/signup**

**Request Body:**

```json
{
  "username": "doctor1",
  "password": "mypassword",
  "role": "DOCTOR"
}
```

**Response Example:**

```json
{
  "id": 1,
  "username": "doctor1",
  "password": "mypassword",
  "role": "DOCTOR",
  "enabled": false
}
```

> Note: `enabled` is false by default; admin approval required.

---

### 2. Approve User (Admin)

**PUT /api/v1/auth/approve/{id}**

**Response Example:**

```json
"User approved successfully!"
```

---

### 3. Get Prescriptions (List)

**GET /api/v1/prescriptions?start=YYYY-MM-DD\&end=YYYY-MM-DD**

**Response Example:**

```json
[
  {
    "id": 1,
    "prescriptionDate": "2025-09-04",
    "patientName": "Rakib",
    "patientAge": 22,
    "patientGender": "Male",
    "diagnosis": "Back Pain",
    "medicines": "Ibuprofen, Paracetamol",
    "nextVisitDate": "2025-09-11",
    "doctorName": "doctor1"
  }
]
```

---

### 4. Get Single Prescription

**GET /api/v1/prescriptions/{id}**

**Response Example:**

```json
{
  "id": 1,
  "prescriptionDate": "2025-09-04",
  "patientName": "Rakib",
  "patientAge": 22,
  "patientGender": "Male",
  "diagnosis": "Back Pain",
  "medicines": "Ibuprofen, Paracetamol",
  "nextVisitDate": "2025-09-11",
  "doctorName": "doctor1"
}
```

---

### 5. Create Prescription (Doctor only)

**POST /api/v1/prescriptions**

**Request Body:**

```json
{
  "prescriptionDate": "2025-09-04",
  "patientName": "Rakib",
  "patientAge": 22,
  "patientGender": "Male",
  "diagnosis": "Back Pain",
  "medicines": "Ibuprofen, Paracetamol",
  "nextVisitDate": "2025-09-11"
}
```

**Response Example:**

```json
{
  "id": 1,
  "prescriptionDate": "2025-09-04",
  "patientName": "Rakib",
  "patientAge": 22,
  "patientGender": "Male",
  "diagnosis": "Back Pain",
  "medicines": "Ibuprofen, Paracetamol",
  "nextVisitDate": "2025-09-11",
  "doctorName": "doctor1"
}
```

---

### 6. Update Prescription (Doctor only)

**PUT /api/v1/prescriptions/{id}**

**Request Body:** (same as create)

**Response:** Updated prescription JSON (same format as above)

---

### 7. Delete Prescription (Doctor only)

**DELETE /api/v1/prescriptions/{id}**

**Response:** `204 No Content` (if deleted successfully)

---

### 8. Day-wise Prescription Report

**GET /api/v1/prescriptions/report/daywise?start=YYYY-MM-DD\&end=YYYY-MM-DD**

**Response Example:**

```json
[
  {
    "day": "2025-09-01",
    "count": 3
  },
  {
    "day": "2025-09-02",
    "count": 5
  }
]
```

---

### Notes

* Only authenticated users can access endpoints. Anonymous access is redirected to login.
* Role-based access is enforced: Doctors create/edit/delete, Patients view only own, Staff/Admin view all.
* Day-wise reports differ for Doctors/Users (filtered by their own prescriptions).
* H2 database console can be accessed at: `http://localhost:8080/h2-console`
* Swagger can be used for testing API requests interactively.

