import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, Input, computed, signal } from '@angular/core';
export interface AreaPoint {
  label: string;
  value: number;
}
/**
 * Small SVG line-and-area chart. Hand-drawn rather than pulling in a charting
 * library, since the dashboard needs exactly one curve.
 */
@Component({
  selector: 'app-area-chart',
  standalone: true,
  imports: [CommonModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="chart">
      <svg [attr.viewBox]="'0 0 ' + W + ' ' + H" preserveAspectRatio="none" class="plot">
        <defs>
          <linearGradient [attr.id]="gradientId" x1="0" y1="0" x2="0" y2="1">
            <stop offset="0%" stop-color="var(--color-accent)" stop-opacity="0.25" />
            <stop offset="100%" stop-color="var(--color-accent)" stop-opacity="0" />
          </linearGradient>
        </defs>
        <line
          *ngFor="let g of gridLines()"
          [attr.x1]="0"
          [attr.x2]="W"
          [attr.y1]="g"
          [attr.y2]="g"
          stroke="var(--color-border)"
          stroke-width="1"
        />
        <path *ngIf="areaPath()" [attr.d]="areaPath()" [attr.fill]="'url(#' + gradientId + ')'" />
        <path
          *ngIf="linePath()"
          [attr.d]="linePath()"
          fill="none"
          stroke="var(--color-accent)"
          stroke-width="2"
          stroke-linejoin="round"
          stroke-linecap="round"
          vector-effect="non-scaling-stroke"
        />
        <circle
          *ngFor="let p of coords()"
          [attr.cx]="p.x"
          [attr.cy]="p.y"
          r="3"
          fill="var(--color-accent)"
          vector-effect="non-scaling-stroke"
        />
      </svg>
      <div class="y-axis">
        <span *ngFor="let t of yTicks()">{{ t }}</span>
      </div>
      <div class="x-axis">
        <span *ngFor="let p of points">{{ p.label }}</span>
      </div>
    </div>
  `,
  styles: [
    `
      .chart {
        position: relative;
        display: grid;
        grid-template-columns: 40px 1fr;
        grid-template-rows: 1fr auto;
        gap: 6px;
        min-height: 240px;
      }
      .plot {
        grid-column: 2;
        grid-row: 1;
        width: 100%;
        height: 100%;
        overflow: visible;
      }
      .y-axis {
        grid-column: 1;
        grid-row: 1;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        align-items: flex-end;
        font-size: 11px;
        color: var(--color-text-muted);
        padding-right: 4px;
      }
      .x-axis {
        grid-column: 2;
        grid-row: 2;
        display: flex;
        justify-content: space-between;
        font-size: 11px;
        color: var(--color-text-muted);
      }
    `,
  ],
})
export class AreaChart {
  readonly W = 600;
  readonly H = 240;
  /** Unique so multiple charts on one page don't share a gradient. */
  readonly gradientId = `area-grad-${Math.random().toString(36).slice(2, 8)}`;
  private readonly data = signal<AreaPoint[]>([]);
  @Input() set points(value: AreaPoint[]) {
    this.data.set(value ?? []);
  }
  get points(): AreaPoint[] {
    return this.data();
  }
  /** Rounded-up ceiling so the curve never touches the top edge. */
  private readonly max = computed(() => {
    const values = this.data().map((p) => p.value);
    const peak = values.length ? Math.max(...values) : 0;
    if (peak <= 0) {
      return 10;
    }
    const step = Math.pow(10, Math.floor(Math.log10(peak)));
    return Math.ceil(peak / step) * step;
  });
  readonly coords = computed(() => {
    const pts = this.data();
    if (!pts.length) {
      return [] as { x: number; y: number }[];
    }
    const max = this.max();
    const stepX = pts.length > 1 ? this.W / (pts.length - 1) : 0;
    return pts.map((p, i) => ({
      x: i * stepX,
      y: this.H - (Math.max(p.value, 0) / max) * this.H,
    }));
  });
  readonly linePath = computed(() => {
    const c = this.coords();
    if (!c.length) {
      return '';
    }
    return c.map((p, i) => `${i === 0 ? 'M' : 'L'}${p.x},${p.y}`).join(' ');
  });
  readonly areaPath = computed(() => {
    const c = this.coords();
    if (!c.length) {
      return '';
    }
    return `${this.linePath()} L${c[c.length - 1].x},${this.H} L${c[0].x},${this.H} Z`;
  });
  readonly yTicks = computed(() => {
    const max = this.max();
    return [4, 3, 2, 1, 0].map((i) => Math.round((max / 4) * i));
  });
  readonly gridLines = computed(() => [0, 1, 2, 3, 4].map((i) => (this.H / 4) * i));
}
