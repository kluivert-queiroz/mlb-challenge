import http from "k6/http";
import { check } from "k6";
import { generateValidCPF } from "./cpf.js";

export const options = {
  scenarios: {
    listUsers: {
      executor: "constant-vus",
      exec: "listUsers",
      vus: 1000,
      duration: '5m',
    },
    findUserById: {
      executor: "constant-vus",
      exec: "findUserById",
      startTime: "10s",
      vus: 1000,
      duration: '5m',
    },
    createUser: {
      executor: "constant-vus",
      exec: "createUser",
      vus: 1000,
      duration: '5m',
    },
    updateUser: {
      executor: "constant-vus",
      exec: "updateUser",
      startTime: "10s",
      vus: 1000,
      duration: '5m',
    },
  },
};

export function listUsers() {
  const response = http.get("http://app:8080/users", {
    headers: { Accepts: "application/json" },
  });
  check(response, { "LIST USERS - status is 200": (r) => r.status === 200 });
}
let createdUserId = null;
export function createUser() {
  const cpf = generateValidCPF();
  const randomNumber = Math.floor(Math.random() * 100000);
  const payload = JSON.stringify({
    name: "John Doe",
    cpf,
    email: `test${randomNumber}@test.com`,
    birthDate: "2000-01-01",
  });
  const response = http.post("http://app:8080/users", payload, {
    headers: {
      Accepts: "application/json",
      "Content-Type": "application/json",
    },
  });
  check(response, {
    "CREATE - status is 201 or 400": (r) => [201, 400].includes(r.status),
  });
  if (response.status === 201) {
    createdUserId = response.json("id");
  }
}
export function findUserById() {
  if (!createdUserId) return;
  const response = http.get(`http://app:8080/users/${createdUserId}`, {
    headers: { Accepts: "application/json" },
  });
  check(response, { "FIND BY ID - status is 200": (r) => r.status === 200 });
}

export function updateUser() {
  if (!createdUserId) return;
  const randomNumber = 1 + Math.floor(Math.random() * 29);
  const payload = JSON.stringify({
    name: "John Doe",
    birthDate: `2000-01-${randomNumber.toString().padStart(2, "0")}`,
  });
  const response = http.put(`http://app:8080/users/${createdUserId}`, payload, {
    headers: {
      Accepts: "application/json",
      "Content-Type": "application/json",
    },
  });
  check(response, { "UPDATE - status is 200": (r) => r.status === 200 })
}
