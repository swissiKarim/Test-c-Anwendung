import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PageContextComponent } from './page-context.component';

describe('PageContextComponent', () => {
  let component: PageContextComponent;
  let fixture: ComponentFixture<PageContextComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PageContextComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PageContextComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
