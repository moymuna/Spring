import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { StorageService } from '../../../services/storage.service';
import { ContactForm } from '../contact-form/contact-form';
@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [CommonModule, RouterLink, ContactForm],
  templateUrl: './landing.html',
  styleUrl: './landing.css',
})
export class Landing implements OnInit {
  stats = [
    { value: '5', label: 'User Roles' },
    { value: '25+', label: 'Modules' },
    { value: '100%', label: 'Audit Trail' },
    { value: '24/7', label: 'Self-service Access' },
  ];
  features = [
    {
      icon: 'bi-people-fill',
      title: 'Employee Management',
      desc: 'A single source of truth for every employee, department, designation and office, with organizational hierarchy and manager reporting lines.',
    },
    {
      icon: 'bi-calendar-check-fill',
      title: 'Attendance Tracking',
      desc: 'Daily check-in/out tracking with automatic status summaries and historical reporting.',
    },
    {
      icon: 'bi-envelope-paper-fill',
      title: 'Leave Management',
      desc: 'Leave requests, approvals, balances and accrual tracking across every leave type.',
    },
    {
      icon: 'bi-cash-coin',
      title: 'Payroll & Payslips',
      desc: 'Automated payroll computation, deductions, and downloadable payslips every cycle.',
    },
    {
      icon: 'bi-briefcase-fill',
      title: 'Recruitment',
      desc: 'Post jobs, collect applications, and move candidates through a structured hiring pipeline.',
    },
    {
      icon: 'bi-mic-fill',
      title: 'Interview Scheduling',
      desc: 'Schedule interviews, assign interviewers, and capture structured feedback and results.',
    },
    {
      icon: 'bi-graph-up-arrow',
      title: 'Performance Reviews',
      desc: 'Structured reviews between employees and managers, with ratings tracked over time.',
    },
    {
      icon: 'bi-mortarboard-fill',
      title: 'Training & Development',
      desc: 'Plan and track employee training programs across departments.',
    },
    {
      icon: 'bi-kanban-fill',
      title: 'Project Management',
      desc: 'Assign employees to projects, track timelines, and manage office-level ownership.',
    },
    {
      icon: 'bi-file-earmark-text-fill',
      title: 'Document Management',
      desc: 'Securely store and retrieve employee documents — contracts, IDs, certificates and more.',
    },
    {
      icon: 'bi-megaphone-fill',
      title: 'Notice Board',
      desc: 'Publish company-wide announcements and holiday notices, office by office.',
    },
    {
      icon: 'bi-shield-lock-fill',
      title: 'Role-based Access',
      desc: 'Admin, HR, Manager, Employee and Applicant each see exactly what they need — nothing more.',
    },
  ];
  constructor(
    private storage: StorageService,
    private router: Router,
  ) {}
  ngOnInit(): void {
    if (this.storage.isLoggedIn()) {
      this.router.navigate(['/dashboard']);
    }
  }
}
