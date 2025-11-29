import {Component, input} from '@angular/core';

@Component({
  selector: 'app-biber',
  imports: [],
  templateUrl: './biber.html',
  styleUrl: './biber.scss',
})
export class Biber {
  readonly name = input<string>('steve');
}
