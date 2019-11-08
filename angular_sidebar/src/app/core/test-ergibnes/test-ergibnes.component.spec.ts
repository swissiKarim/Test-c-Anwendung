import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TestErgibnesComponent } from './test-ergibnes.component';

describe('TestErgibnesComponent', () => {
  let component: TestErgibnesComponent;
  let fixture: ComponentFixture<TestErgibnesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TestErgibnesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TestErgibnesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
