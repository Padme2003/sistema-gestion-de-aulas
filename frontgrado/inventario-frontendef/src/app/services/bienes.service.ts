import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BienesService {

  private apiUrl = 'http://localhost:8080/api/bienes';

  constructor(private http: HttpClient) {}

  getAll(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  create(bien: any): Observable<any> {
    return this.http.post(this.apiUrl, bien);
  }

  update(id: number, bien: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, bien);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
 getParaEditar(id: number) {
  return this.http.get<any>(`${this.apiUrl}/editar/${id}`);
}


}
