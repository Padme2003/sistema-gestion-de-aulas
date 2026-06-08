import { Bien } from './bien.model';
export interface Solicitud {
  idSolicitud: number;
  descripcion?: string;
  estado?: 'PENDIENTE' | 'APROBADA' | 'DENEGADA';
  fecha?: string; // fecha de la solicitud
  bien?: Bien;
  docente?: { nombre: string }; // nombre del docente que pidió la solicitud
    fechaSolicitud?: string;
  usuario: {
    idUsuario: number;
    nombre: string;
  };
}
