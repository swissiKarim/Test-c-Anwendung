import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TeilnehmerlisteComponent } from './teilnehmerliste.component';

describe('TeilnehmerlisteComponent', () => {
  let component: TeilnehmerlisteComponent;
  let fixture: ComponentFixture<TeilnehmerlisteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TeilnehmerlisteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TeilnehmerlisteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
