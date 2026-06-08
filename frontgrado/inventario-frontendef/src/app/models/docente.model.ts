import { Usuario } from './usuario.model';

export interface Docente {
  id_docente: number;
  nombres: string;
  apellidos: string;
  cedula: string;
  regimen: string;
  observaciones: string;
  id_usuario: number;
  usuario?: Usuario;
}
