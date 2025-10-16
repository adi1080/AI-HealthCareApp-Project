import { Component } from '@angular/core';
import { AdminService, User } from '../../Services/admin.service';
import { DoctorService } from 'src/app/Doctor/Services/doctor.service';

@Component({
  selector: 'app-doctors',
  templateUrl: './doctors.component.html',
  styleUrls: ['./doctors.component.scss']
})
export class DoctorsComponent {
  users: User[] = [];
  DoctorList: User[] = [];
  loading: boolean = true;

  constructor(
    private adminService: AdminService,
    private doctorService: DoctorService
  ) {}

  ngOnInit(): void {
    // 🧠 Step 1: Trigger backend feedback analysis automatically
    this.adminService.triggerSmartAnalysis().subscribe({
      next: (res) => {
        console.log('✅ Feedback analysis triggered:', res);

        // 🧠 Step 2: Once done, load doctors list
        this.loadDoctors();
      },
      error: (err) => {
        console.error('❌ Failed to trigger feedback analysis:', err);
        // Even if it fails, still try to load doctors
        this.loadDoctors();
      }
    });
  }

  // ✅ Load doctors list from backend
  loadDoctors(): void {
    this.adminService.getAllUsers().subscribe({
      next: (list: User[]) => {
        console.log('All Users:', list);
        this.users = list;
        this.DoctorList = list.filter((user) => user.role?.toLowerCase() === 'doctor');
        console.log('Doctors:', this.DoctorList);
        this.loading = false;
      },
      error: (error) => {
        console.error('Error fetching users:', error);
        this.loading = false;
      }
    });
  }

  // ✅ Permit doctor (set access = true)
  permitDoctor(doctorId: number): void {
    this.adminService.permit(doctorId).subscribe({
      next: () => {
        console.log(`✅ Doctor ${doctorId} permitted`);
        alert("Doctor permitted");
        this.loadDoctors();
      },
      error: (err) => console.error('❌ Permit failed:', err)
    });
  }

  // ✅ Block doctor (set access = false)
  blockDoctor(doctorId: number): void {
    this.adminService.block(doctorId).subscribe({
      next: () => {
        console.log(`🚫 Doctor ${doctorId} blocked`);
        alert("Doctor Access Blocked");
        this.loadDoctors();
      },
      error: (err) => console.error('❌ Block failed:', err)
    });
  }
}