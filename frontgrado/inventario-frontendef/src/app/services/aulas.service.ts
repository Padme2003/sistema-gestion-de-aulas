import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Aula } from '../models/aula.model';

@Injectable({
  providedIn: 'root'
})
export class AulasService {

  private baseUrl = 'http://localhost:8080/api/aulas';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`
    });
  }

  // 📌 Obtener todas las aulas
  getAll(): Observable<Aula[]> {
    return this.http.get<Aula[]>(this.baseUrl, {
      headers: this.getHeaders()
    });
  }

  // 📌 Obtener aula por ID
  getById(id: number): Observable<Aula> {
    return this.http.get<Aula>(`${this.baseUrl}/${id}`, {
      headers: this.getHeaders()
    });
  }

  // 📌 Crear aula
  create(aula: Aula): Observable<Aula> {
    return this.http.post<Aula>(this.baseUrl, aula, {
      headers: this.getHeaders()
    });
  }

  // 📌 Actualizar aula
  update(id: number, aula: Aula): Observable<Aula> {
    return this.http.put<Aula>(`${this.baseUrl}/${id}`, aula, {
      headers: this.getHeaders()
    });
  }

  // 📌 Eliminar aula
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`, {
      headers: this.getHeaders()
    });
  }
}
