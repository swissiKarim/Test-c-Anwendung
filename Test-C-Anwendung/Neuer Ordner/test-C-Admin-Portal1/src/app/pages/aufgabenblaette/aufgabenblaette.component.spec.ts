import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AufgabenblaetteComponent } from './aufgabenblaette.component';

describe('AufgabenblaetteComponent', () => {
  let component: AufgabenblaetteComponent;
  let fixture: ComponentFixture<AufgabenblaetteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AufgabenblaetteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AufgabenblaetteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
