import { ChangeDetectionStrategy, Component, ElementRef, ViewChild, effect, input } from '@angular/core';
import { ChartPoint } from '../../../core/model';

@Component({
  selector: 'app-line-chart',
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: {
    class: 'line-chart',
    role: 'img',
    'aria-label': 'Line chart'
  },
  templateUrl: './line-chart.component.html',
  styleUrls: ['./line-chart.component.scss']
})
export class LineChartComponent {
  readonly points = input<ChartPoint[]>([]);
  @ViewChild('canvas', { static: true }) canvas!: ElementRef<HTMLCanvasElement>;

  constructor() {
    effect(() => this.draw(this.points()));
  }

  private draw(points: ChartPoint[]) {
    const canvas = this.canvas?.nativeElement;
    if (!canvas) return;
    const dpr = window.devicePixelRatio || 1;
    const width = canvas.clientWidth;
    const height = 160;
    canvas.width = Math.floor(width * dpr);
    canvas.height = Math.floor(height * dpr);
    const ctx = canvas.getContext('2d');
    if (!ctx) return;
    ctx.scale(dpr, dpr);
    ctx.clearRect(0, 0, width, height);
    ctx.strokeStyle = getComputedStyle(document.documentElement).getPropertyValue('--border') || '#374151';
    ctx.lineWidth = 1;
    ctx.beginPath();
    ctx.moveTo(32, 8);
    ctx.lineTo(32, height - 24);
    ctx.lineTo(width - 8, height - 24);
    ctx.stroke();
    if (points.length) {
      const maxX = Math.max(...points.map(p => p.x));
      const maxY = Math.max(...points.map(p => p.y));
      const scaleX = (width - 48) / Math.max(1, maxX);
      const scaleY = (height - 48) / Math.max(1, maxY);
      ctx.strokeStyle = getComputedStyle(document.documentElement).getPropertyValue('--primary') || '#60a5fa';
      ctx.lineWidth = 2;
      ctx.beginPath();
      points.forEach((p, i) => {
        const x = 32 + p.x * scaleX;
        const y = height - 24 - p.y * scaleY;
        if (i === 0) ctx.moveTo(x, y); else ctx.lineTo(x, y);
      });
      ctx.stroke();
    }
  }
}
