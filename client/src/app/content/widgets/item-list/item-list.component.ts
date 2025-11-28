import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { ListItem } from '../../../core/model';

@Component({
  selector: 'app-item-list',
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: {
    class: 'item-list',
    role: 'list',
    'aria-label': 'Resource list'
  },
  templateUrl: './item-list.component.html',
  styleUrls: ['./item-list.component.scss']
})
export class ItemListComponent {
  readonly items = input<ListItem[]>([]);
}
