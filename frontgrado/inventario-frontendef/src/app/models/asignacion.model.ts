import { Aula } from './aula.model';
import { Usuario } from './usuario.model';

export interface Asignacion {
  idAsignacion: number;
  id_usuario: number;
  id_aula: number;
  fechaSolicitud:  string;
  estado: boolean;
  usuario: Usuario;
  aula: Aula;
}
