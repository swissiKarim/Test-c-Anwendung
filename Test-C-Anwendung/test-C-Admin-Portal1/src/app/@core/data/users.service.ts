
import { of as observableOf,  Observable } from 'rxjs';
import { Injectable } from '@angular/core';

class User {
  name: string;
  picture: string;
}

export class Contacts {
  user: User;
  type: string;
}

export class RecentUsers extends Contacts {
  time: number;
}

@Injectable()
export class UserService {

  private time: Date = new Date;

  private users = {
    swissi: { name: 'swissi', picture: 'assets/images/ser.jpg' },

  };
  private types = {
    mobile: 'mobile',
    home: 'home',
    work: 'work',
  };
  private contacts: Contacts[] = [
    { user: this.users.swissi, type: this.types.mobile },
  ];
  private recentUsers: RecentUsers[]  = [
    { user: this.users.swissi, type: this.types.mobile, time: this.time.setHours(5, 29)},
  ];

  getUsers(): Observable<any> {
    return observableOf(this.users);
  }

  getContacts(): Observable<Contacts[]> {
    return observableOf(this.contacts);
  }

  getRecentUsers(): Observable<RecentUsers[]> {
    return observableOf(this.recentUsers);
  }
}
