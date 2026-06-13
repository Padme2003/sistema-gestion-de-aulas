import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable({ providedIn: 'root' })
export class CategoriasService {

  private apiUrl = 'https://inventario-backend-k101.onrender.com/api/categorias';

  constructor(private http: HttpClient) {}

  getAll() {
    return this.http.get<any[]>(this.apiUrl);
  }

  create(categoria: any) {
    return this.http.post(this.apiUrl, categoria);
  }
}
