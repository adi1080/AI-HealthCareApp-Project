import { Component } from '@angular/core';
import { AdminService, User } from '../../Services/admin.service';
import { MessageService } from 'src/app/Services/message.service';

@Component({
  selector: 'app-blocked',
  templateUrl: './blocked.component.html',
  styleUrls: ['./blocked.component.scss']
})
export class BlockedComponent {
 users: User[] = [];
  BlockedFolks: User[] = [];

  constructor(private adminService: AdminService, private messageService:MessageService) { }

  ngOnInit(): void {
    this.adminService.getAllUsers().subscribe(
      (response) => {
        this.BlockedFolks = response.filter(user => user.permitted === false).map(users => users);
        console.log("Blocked Folks : ",this.BlockedFolks);
      }
    )
  }

    permit(id: number): void {
    this.adminService.permit(id).subscribe({
      next: () => {
        console.log(`✅ User ${id} permitted`);
        this.messageService.showMessage(" User Access Permitted",3000);
        window.location.reload();
      },
      error: (err) => console.error('❌ Permit failed:', err)
    });
  }
}
