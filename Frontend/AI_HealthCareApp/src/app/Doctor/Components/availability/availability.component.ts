import { Component } from '@angular/core';

@Component({
  selector: 'app-availability',
  templateUrl: './availability.component.html',
  styleUrls: ['./availability.component.scss']
})
export class AvailabilityComponent {
availabilityList: { date: string, time: string }[] = [];
setAvailability = false;
newDateTime: string = '';

doctorId:any = localStorage.getItem("DocId");

constructor() { }

popupAvailabilityInput() {
    this.setAvailability = !this.setAvailability;
  }

  addAvailability() {
    if (this.newDateTime) {
      const dt = new Date(this.newDateTime);
      const date = dt.toLocaleDateString();
      const time = dt.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
      this.availabilityList.push({ date, time });

      // Reset input
      this.newDateTime = '';
      this.setAvailability = false;
    }
  }

  removeAvailability(index: number) {
    this.availabilityList.splice(index, 1);
  }

}
