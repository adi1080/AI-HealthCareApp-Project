import { Component } from '@angular/core';
import { AdminService, User } from '../../Services/admin.service';
import { DoctorService } from 'src/app/Doctor/Services/doctor.service';
import { MessageService } from 'src/app/Services/message.service';

@Component({
  selector: 'app-patients',
  templateUrl: './patients.component.html',
  styleUrls: ['./patients.component.scss']
})
export class PatientsComponent {
  users: User[] = [];
  PatientList: User[] = [];

  constructor(private adminService: AdminService, private doctorService: DoctorService, private messageService:MessageService) { }

  ngOnInit(): void {
    this.adminService.getAllUsers().subscribe(
      (list: User[]) => {
        this.users = list;

        list.forEach(user => {
          if (user.role == "User") {
            this.PatientList.push(user);
          }
        });
        console.log("patients : ", this.PatientList);
      },
      (error) => {
        console.error("Error fetching users:", error);
      }
    );

  }

  permitPatient(id: number): void {
    this.adminService.permit(id).subscribe({
      next: () => {
        console.log(`✅ Patient ${id} permitted`);
        this.messageService.showMessage("Patient Access Permitted",3000);
        window.location.reload();
      },
      error: (err) => console.error('❌ Permit failed:', err)
    });
  }

  blockPatient(id: number): void {
    this.adminService.block(id).subscribe({
      next: () => {
        console.log(`🚫 Patient ${id} blocked`);
       this.messageService.showMessage("Patient Access Blocked", 3000);
        window.location.reload();
      },
      error: (err) => console.error('❌ Block failed:', err)
    });
  }
}
