import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, Input, computed, signal } from '@angular/core';
export interface DonutSegment {
  label: string;
  value: number;
  color?: string;
}
/** Palette used when a segment doesn't bring its own colour. */
const DEFAULT_COLORS = [
  '#1f6fe5',
  '#16a34a',
  '#f59e0b',
  '#8b5cf6',
  '#ef4444',
  '#06b6d4',
  '#ec4899',
  '#64748b',
];
/** SVG doughnut with a centre total and a side legend. No charting dependency. */
@Component({
  selector: 'app-donut-chart',
  standalone: true,
  imports: [CommonModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="donut-wrap">
      <div class="donut">
        <svg viewBox="0 0 120 120">
          <circle cx="60" cy="60" [attr.r]="R" fill="none" stroke="var(--color-bg-subtle)" stroke-width="18" />
          <circle
            *ngFor="let a of arcs()"
            cx="60"
            cy="60"
            [attr.r]="R"
            fill="none"
            [attr.stroke]="a.color"
            stroke-width="18"
            [attr.stroke-dasharray]="a.dash"
            [attr.stroke-dashoffset]="a.offset"
            transform="rotate(-90 60 60)"
            stroke-linecap="butt"
          />
        </svg>
        <div class="center">
          <strong>{{ total() }}</strong>
          <small>{{ centerLabel }}</small>
        </div>
      </div>
      <ul class="legend">
        <li *ngFor="let a of arcs()">
          <span class="dot" [style.background]="a.color"></span>
          <span class="label">{{ a.label }}</span>
          <span class="value">{{ a.value }} ({{ a.percent }}%)</span>
        </li>
        <li class="empty" *ngIf="arcs().length === 0">No data yet.</li>
      </ul>
    </div>
  `,
  styles: [
    `
      .donut-wrap {
        display: flex;
        align-items: center;
        gap: 24px;
        flex-wrap: wrap;
      }
      .donut {
        position: relative;
        width: 170px;
        height: 170px;
        flex-shrink: 0;
      }
      .donut svg {
        width: 100%;
        height: 100%;
      }
      .center {
        position: absolute;
        inset: 0;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        line-height: 1.1;
      }
      .center strong {
        font-size: 26px;
        font-weight: 700;
        color: var(--color-text-primary);
      }
      .center small {
        font-size: 11px;
        color: var(--color-text-muted);
      }
      .legend {
        list-style: none;
        margin: 0;
        padding: 0;
        flex: 1;
        min-width: 190px;
        display: flex;
        flex-direction: column;
        gap: 9px;
      }
      .legend li {
        display: flex;
        align-items: center;
        gap: 9px;
        font-size: 13px;
        color: var(--color-text-secondary);
      }
      .dot {
        width: 10px;
        height: 10px;
        border-radius: 50%;
        flex-shrink: 0;
      }
      .label {
        flex: 1;
        color: var(--color-text-primary);
      }
      .value {
        font-weight: 600;
        white-space: nowrap;
      }
      .legend .empty {
        color: var(--color-text-muted);
      }
    `,
  ],
})
export class DonutChart {
  readonly R = 45;
  private readonly CIRC = 2 * Math.PI * 45;
  @Input() centerLabel = 'Total';
  private readonly data = signal<DonutSegment[]>([]);
  @Input() set segments(value: DonutSegment[]) {
    this.data.set((value ?? []).filter((s) => s.value > 0));
  }
  readonly total = computed(() => this.data().reduce((sum, s) => sum + s.value, 0));
  readonly arcs = computed(() => {
    const total = this.total();
    if (!total) {
      return [];
    }
    let consumed = 0;
    return this.data().map((s, i) => {
      const fraction = s.value / total;
      const length = fraction * this.CIRC;
      // Offset walks each arc round the circle so they sit end to end.
      const arc = {
        label: s.label,
        value: s.value,
        color: s.color ?? DEFAULT_COLORS[i % DEFAULT_COLORS.length],
        percent: Math.round(fraction * 1000) / 10,
        dash: `${length} ${this.CIRC - length}`,
        offset: -consumed,
      };
      consumed += length;
      return arc;
    });
  });
}
