import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DoctorService } from '../../Services/doctor.service';

@Component({
  selector: 'app-availability',
  templateUrl: './availability.component.html',
  styleUrls: ['./availability.component.scss']
})
export class AvailabilityComponent implements OnInit {
  availabilityList: {id:any , date: string, time: string, isBooked: boolean }[] = [];
  availabilityForm!: FormGroup;
  setAvailability = false;
  doctorId = localStorage.getItem("LoggedIndocId");
  
  constructor(private fb: FormBuilder, private docService: DoctorService) { }

  ngOnInit(): void {
    // Initialize the form with a dateTime field
    this.availabilityForm = this.fb.group({
      dateTime: ['', Validators.required]
    });
    console.log(this.doctorId);
    // Fetch all availability when the component initializes/opens
    this.findAllAvailability();
  }


  //opening popup
  popupAvailabilityInput(): void {
    this.setAvailability = true;
  }

  addAvailability(): void {
    const dateTime = this.availabilityForm.get('dateTime')?.value;
    if (dateTime) {
      const [date, time] = dateTime.split('T');

      const availability = {
        date,
        time,
        isBooked: false
      };

      console.log("availability =>",availability);
      this.docService.addAvailability(this.doctorId, availability).subscribe(
        response => {
          console.log(response);
        },
        error => {
          console.error('Error adding availability:', error);
        }
        );
    }
    
    this.setAvailability = false;
    setTimeout(() => {
      window.location.reload();
    }, 500);
    this.findAllAvailability();
  }

  findAllAvailability() {
     this.docService.FindAllAvailability(this.doctorId).subscribe(
      response => {
        console.log('Fetched availability:', response);
        // Clear the existing availabilityList before adding new items
        this.availabilityList = [];
        this.availabilityList = response.map((item: any) => ({
          id: item.id,
          date: item.date,
          time: item.time,
          isBooked: item.isBooked
        }));
        console.log("availabilityList =>",this.availabilityList);

        // Sort the availabilityList by date and time
        this.availabilityList.sort((a, b) => {
          const dateA = new Date(`${a.date}T${a.time}`);
          const dateB = new Date(`${b.date}T${b.time}`);
          return dateA.getTime() - dateB.getTime();
        });
      }
     )
  }

  removeAvailability(index: number): void {
     const availabilityId = this.availabilityList[index].id;
    console.log("availabilityId =>",availabilityId);
    this.docService.DeleteAvailabilityById(availabilityId).subscribe();
    window.location.reload();
  }
}
