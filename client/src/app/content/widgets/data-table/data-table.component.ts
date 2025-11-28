import { ChangeDetectionStrategy, Component, computed, input, signal } from '@angular/core';
import { TableColumn, TableRow } from '../../../core/model';

@Component({
  selector: 'app-data-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: {
    class: 'data-table',
    role: 'region',
    'aria-label': 'Data table'
  },
  templateUrl: './data-table.component.html',
  styleUrls: ['./data-table.component.scss']
})
export class DataTableComponent {
  readonly columns = input<TableColumn[]>([]);
  readonly rows = input<TableRow[]>([]);

  private sortKey = signal<string | null>(null);
  private sortAsc = signal<boolean>(true);

  readonly sortedRows = computed(() => {
    const data = [...this.rows()];
    const key = this.sortKey();
    const asc = this.sortAsc();
    if (!key) return data;
    return data.sort((a, b) => {
      const av = a[key];
      const bv = b[key];
      const cmp = String(av).localeCompare(String(bv), undefined, { numeric: true });
      return asc ? cmp : -cmp;
    });
  });

  toggleSort(key: string) {
    if (this.sortKey() === key) {
      this.sortAsc.update(v => !v);
    } else {
      this.sortKey.set(key);
      this.sortAsc.set(true);
    }
  }
}
