export interface Notificacion {
  id_notificacion: number;
  tipo_notificacion: string;
  bienes_constatados: number;
  bienes_no_identificados: number;
  cantidad_olympo_2015: number;
  cantidad_olympo_senescyt: number;
  cantidad_otro_sistema: number;
  total_sistema: number;
  total: number;
  cuenta_contable: string;
  nombre_cuenta: string;
  sistema_control: string;
  fecha_creacion: Date | string;
  leida: boolean;
}
