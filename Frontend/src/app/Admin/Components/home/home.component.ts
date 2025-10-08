import { Component, OnInit } from '@angular/core';
import { AdminService, User } from '../../Services/admin.service';
import { DoctorService } from 'src/app/Doctor/Services/doctor.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  users: User[] = [];
  PatientList:User[] = [];
  DoctorList:User[] = [];

  constructor(private adminService: AdminService, private doctorService:DoctorService) {}

  ngOnInit(): void {
    this.adminService.getAllUsers().subscribe(
      (list: User[]) => {
        console.log("All Users:", list);
        this.users = list;

        list.forEach(user => {
          console.log("User Role:", user.role);

          if(user.role == "User"){
            this.PatientList.push(user);
          }
          if(user.role == "Doctor"){
            this.DoctorList.push(user);
          }
          
          console.log("doctors : ", this.DoctorList);
          console.log("patients : ", this.PatientList);
        });
      },
      (error) => {
        console.error("Error fetching users:", error);
      }
    );

    this.doctorService.getAllFeedbacks().subscribe(
      (response) => {
        console.log("feedbacks : " , response);
      }
    );

  }
}
