import { Injectable, computed, signal } from '@angular/core';
import { ApiService } from '../core/service/api.service';
import { HttpParams } from '@angular/common/http';
import { Metric, ChartPoint, TableColumn, TableRow, ListItem } from '../core/model';

@Injectable({ providedIn: 'root' })
export class ContentService {
  private baseUrl = '/api';

  readonly metrics = signal<Metric[]>([
    { id: 'cpu', label: 'CPU', value: 37, unit: '%', trend: 'up' },
    { id: 'mem', label: 'Memory', value: 62, unit: '%', trend: 'down' },
    { id: 'req', label: 'Requests', value: 1240, trend: 'up' }
  ]);

  readonly chartData = signal<ChartPoint[]>(Array.from({ length: 30 }, (_, i) => ({ x: i, y: Math.round(30 + 15 * Math.sin(i / 4)) })));

  readonly tableColumns = signal<TableColumn[]>([
    { key: 'service', label: 'Service' },
    { key: 'status', label: 'Status' },
    { key: 'latency', label: 'Latency (ms)' }
  ]);
  readonly tableRows = signal<TableRow[]>([
    { service: 'auth', status: 'ok', latency: 22 },
    { service: 'billing', status: 'warn', latency: 145 },
    { service: 'search', status: 'ok', latency: 33 }
  ]);

  readonly listItems = signal<ListItem[]>([
    { id: '1', title: 'Node-1', subtitle: 'eu-west-1a', status: 'ok' },
    { id: '2', title: 'Node-2', subtitle: 'eu-west-1b', status: 'warn' },
    { id: '3', title: 'Node-3', subtitle: 'eu-west-1c', status: 'error' }
  ]);

  readonly totals = computed(() => ({
    services: this.tableRows().length,
    nodes: this.listItems().length,
    avgLatency: Math.round(
      this.tableRows().reduce((acc, r) => acc + (Number(r["latency"]) || 0), 0) / Math.max(1, this.tableRows().length)
    )
  }));

  constructor(private api: ApiService) {}

  refreshMetrics(path = '/metrics') {
    const params = new HttpParams();
    this.api.get(this.baseUrl, path, params).subscribe({
      next: (data: any) => {
        if (Array.isArray(data)) {
          this.metrics.set(data as Metric[]);
        }
      }
    });
  }

  refreshChart(path = '/chart') {
    this.api.get(this.baseUrl, path).subscribe({
      next: (data: any) => {
        if (Array.isArray(data)) this.chartData.set(data as ChartPoint[]);
      }
    });
  }

  refreshTable(path = '/services') {
    this.api.get(this.baseUrl, path).subscribe({
      next: (data: any) => {
        if (Array.isArray(data)) this.tableRows.set(data as TableRow[]);
      }
    });
  }

  refreshList(path = '/nodes') {
    this.api.get(this.baseUrl, path).subscribe({
      next: (data: any) => {
        if (Array.isArray(data)) this.listItems.set(data as ListItem[]);
      }
    });
  }
}
