# Prescription Management System

**Prescription Management System** is a simple web-based application for generating, managing and reporting medical prescriptions. It allows authenticated users to create, edit, delete, and view prescriptions in a secure environment. The system also provides REST APIs for integration and supports day-wise prescription reports.

---

## Features

* User authentication (no anonymous access)
  
     * **Have pre-stored user credentials data in data.sql**
     * **User can signup, but they need approval from database adminestrator(make ENABLE=TRUE if account creation request you want to approve), after approval can login.**
     * **Can add,modify,delete user credentials from database**
     * **Have option to make password encrypted so that database adminestretor can't see real password**
       
* Create, edit, delete prescriptions
* Day-wise prescription report
* REST API endpoint: `/api/v1/prescriptions` (JSON response)
* Role-based access control:

  * **Doctor**: create, edit, delete prescriptions
  * **Pharmacist/Medical Staff/Admin**: view all prescriptions

* Swagger API documentation
* Responsive UI with form validation
* Integration with RxNav API for drug interactions

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

---

## Using Swagger for API Documentation

1. **Open Swagger UI:**

```
http://localhost:8080/swagger-ui/index.html
```

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

