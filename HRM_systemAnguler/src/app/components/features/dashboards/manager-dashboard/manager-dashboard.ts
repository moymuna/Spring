import { Component, OnInit, ChangeDetectorRef, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ProjectService } from '../../../../services/project.service';
import { PerformancereviewService } from '../../../../services/performancereview.service';
@Component({
  selector: 'app-manager-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './manager-dashboard.html',
  styleUrl: './manager-dashboard.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ManagerDashboard implements OnInit {
  projects: any[] = [];
  reviews: any[] = [];
  constructor(
    private projectService: ProjectService,
    private reviewService: PerformancereviewService,
    private cdr: ChangeDetectorRef,
  ) {}
  ngOnInit(): void {
    this.loadManagerData();
  }
  loadManagerData(): void {
    this.projectService.getAllProjects().subscribe({
      next: (data: any) => {
        this.projects = data ? data.slice(0, 5) : [];
        this.cdr.detectChanges();
      },
    });
    this.reviewService.getAllPerformanceReviews().subscribe({
      next: (data: any) => {
        this.reviews = data ? data.slice(0, 5) : [];
        this.cdr.detectChanges();
      },
    });
  }
}
