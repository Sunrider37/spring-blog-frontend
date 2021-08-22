import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PostPayload } from './add-post/post-payload';

@Injectable({
  providedIn: 'root'
})
export class AddPostService {
  private url = 'http://localhost:8080/api/posts'

  constructor(private httpClient: HttpClient) {

   }

   addPost(postPayload: PostPayload):Observable<any>{
    return this.httpClient.post(this.url, postPayload, {
      responseType: 'text'
    });
   }

   getAllPosts(): Observable<Array<PostPayload>>{
    return this.httpClient.get<Array<PostPayload>>(this.url + "/all")
   }

   getPost(permaLink: Number) : Observable<PostPayload>{
     return this.httpClient.get<PostPayload>(this.url + '/' + permaLink);
   }
}
