const BASE=import.meta.env.VITE_API_URL??'/api';
export async function api<T>(path:string,options?:RequestInit):Promise<T>{const res=await fetch(`${BASE}${path}`,{credentials:'include',headers:{'Content-Type':'application/json',...options?.headers},...options});const body=await res.json().catch(()=>({message:'Something went wrong'}));if(!res.ok)throw new Error(body.message);return body as T;}
